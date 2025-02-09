package app

import (
	"fmt"
	"time"

	"go.temporal.io/sdk/temporal"
	"go.temporal.io/sdk/workflow"
)

const MoneyTransferTaskQueueName = "TRANSFER_MONEY_TASK_QUEUE"

type PaymentDetails struct {
	SourceAccount string
	TargetAccount string
	Amount        int
	ReferenceID   string
}

func MoneyTransfer(ctx workflow.Context, input PaymentDetails) (string, error) {
	retryPolicy := &temporal.RetryPolicy{
		InitialInterval:        time.Second,
		BackoffCoefficient:     2.0,
		MaximumInterval:        60 * time.Second,
		MaximumAttempts:        3,
		NonRetryableErrorTypes: []string{"InvalidAccountError", "InsufficientFundsError"},
	}

	options := workflow.ActivityOptions{
		StartToCloseTimeout: time.Minute,
		RetryPolicy:         retryPolicy,
	}

	ctx = workflow.WithActivityOptions(ctx, options)
	var withdrawOutput string

	withdrawErr := workflow.ExecuteActivity(ctx, Withdraw, input).Get(ctx, &withdrawOutput)
	if withdrawErr != nil {
		return "", withdrawErr
	}

	var depositOutput string
	depositErr := workflow.ExecuteActivity(ctx, Deposit, input).Get(ctx, &depositOutput)
	if depositErr == nil {
		result := fmt.Sprintf("Transfer complete (transaction IDs: %s, %s)", withdrawOutput, depositOutput)
		return result, nil
	}

	var result string
	refundErr := workflow.ExecuteActivity(ctx, Refund, input).Get(ctx, &result)
	if refundErr != nil {
		return "", fmt.Errorf("Deposit: failed to deposit money into %v: %v. Money could not be returned to %v: %w",
			input.TargetAccount, depositErr, input.SourceAccount, refundErr)
	}
	return "", fmt.Errorf("Deposit: failed to deposit money into %v: Money returned to %v: %w",
		input.TargetAccount, input.SourceAccount, depositErr)
}
