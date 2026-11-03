// ffff0000907c2f94  size=32  FUN_ffff0000907c2f94

void FUN_ffff0000907c2f94(long param_1)

{
  code *UNRECOVERED_JUMPTABLE;
  
  UNRECOVERED_JUMPTABLE = *(code **)(*(long *)(param_1 + 8) + 0x18);
  if (UNRECOVERED_JUMPTABLE != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff0000907c2fa8. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    (*UNRECOVERED_JUMPTABLE)();
    return;
  }
  return;
}


