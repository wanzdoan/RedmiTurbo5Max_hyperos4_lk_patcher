// ffff00009077c210  size=32  FUN_ffff00009077c210

undefined8 FUN_ffff00009077c210(long param_1)

{
  undefined8 uVar1;
  
  if (*(code **)(param_1 + 0x98) != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff00009077c220. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    uVar1 = (**(code **)(param_1 + 0x98))();
    return uVar1;
  }
  return 0xffffffe8;
}


