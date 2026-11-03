
undefined8 FUN_ffff0000b800ee90(undefined8 param_1,uint param_2)

{
  undefined8 uVar1;
  long lVar2;
  code *UNRECOVERED_JUMPTABLE;
  
  if (((param_2 < 2) && ((&DAT_ffff0000b811ae80)[param_2] != 0)) &&
     (lVar2 = *(long *)((&DAT_ffff0000b811ae80)[param_2] + 0x18), lVar2 != 0)) {
    UNRECOVERED_JUMPTABLE = *(code **)(lVar2 + 8);
    if (UNRECOVERED_JUMPTABLE != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff0000b800eec0. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      uVar1 = (*UNRECOVERED_JUMPTABLE)();
      return uVar1;
    }
    return 0xffffff7a;
  }
  return 0xffffffea;
}


