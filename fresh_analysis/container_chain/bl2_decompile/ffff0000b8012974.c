
undefined8 FUN_ffff0000b8012974(void)

{
  undefined8 uVar1;
  code *UNRECOVERED_JUMPTABLE;
  
  if ((DAT_ffff0000b811af30 != 0) && (*(long *)(DAT_ffff0000b811af30 + 0x28) != 0)) {
    UNRECOVERED_JUMPTABLE = *(code **)(*(long *)(DAT_ffff0000b811af30 + 0x28) + 0x18);
    if (UNRECOVERED_JUMPTABLE != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff0000b8012998. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      uVar1 = (*UNRECOVERED_JUMPTABLE)();
      return uVar1;
    }
    return 0xffffff7a;
  }
  return 0xffffffea;
}


