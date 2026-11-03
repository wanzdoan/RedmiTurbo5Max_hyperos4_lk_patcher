
undefined8 FUN_ffff0000b80127ac(uint param_1)

{
  undefined8 uVar1;
  code *UNRECOVERED_JUMPTABLE;
  
  if ((DAT_ffff0000b811af38 != 0) && (*(long *)(DAT_ffff0000b811af38 + 0x28) != 0)) {
    UNRECOVERED_JUMPTABLE = *(code **)(*(long *)(DAT_ffff0000b811af38 + 0x28) + 0x38);
    if (UNRECOVERED_JUMPTABLE != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff0000b80127d4. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      uVar1 = (*UNRECOVERED_JUMPTABLE)(param_1 & 1);
      return uVar1;
    }
    return 0xffffff7a;
  }
  return 0xffffffea;
}


