
undefined8 FUN_ffff0000b804ab84(long param_1)

{
  undefined8 uVar1;
  
  if (*(code **)(param_1 + 0x98) != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff0000b804ab94. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    uVar1 = (**(code **)(param_1 + 0x98))();
    return uVar1;
  }
  return 0xffffffe8;
}


