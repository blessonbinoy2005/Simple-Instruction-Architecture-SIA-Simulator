# Simple Instruction Architecture (SIA) Simulator

This project implements a simulator for the **Simple Instruction Architecture (SIA)** — a minimalist CPU architecture designed for ease of assembly, virtual machine implementation, and learning computer architecture fundamentals.

SIA features a small instruction set (under 20 instructions), 32 general-purpose 32-bit registers, and simple instruction formats aimed at beginners.

## Features

- Supports 32 general-purpose 32-bit registers (register 0 is general-purpose, not fixed zero)
- Implements a minimal instruction set (~20 instructions), including arithmetic, logic, branch, call/return, and memory access
- Simulates an infinite call stack for subroutine management (via Call and Return instructions)
- Status register updated via Compare instructions, used for conditional branching
- Supports two instruction formats:  
  - 2-register (2R) instructions  
  - Immediate instructions  
- Simple virtual CPU execution model (fetch-decode-execute cycle)

## Instruction Formats and Opcodes

- 2R format instructions operate on two registers.
- Immediate instructions include an immediate value.
- Call and Return instructions manage control flow with the call stack.
- All instructions update registers and status flags as appropriate.
