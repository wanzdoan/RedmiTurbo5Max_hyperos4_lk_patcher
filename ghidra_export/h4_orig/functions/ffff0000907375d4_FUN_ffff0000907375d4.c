// ffff0000907375d4  size=40  FUN_ffff0000907375d4

undefined8 FUN_ffff0000907375d4(long param_1,long param_2)

{
  undefined8 uVar1;
  
  if (*(code **)(param_1 + 0x10) != (code *)0x0) {
    if (*(long *)(param_2 + 0x10) != 0) {
      param_2 = *(long *)(param_2 + 0x10);
    }
                    /* WARNING: Could not recover jumptable at 0xffff0000907375f0. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    uVar1 = (**(code **)(param_1 + 0x10))(param_2);
    return uVar1;
  }
  return 0xfffffff8;
}


