
undefined8 FUN_ffff0000be05e088(long param_1,undefined8 param_2,long param_3,long param_4)

{
  undefined8 uVar1;
  long lVar2;
  
  if ((((-1 < param_3) && (param_4 != 0)) && (lVar2 = *(long *)(param_1 + 0x20), param_3 < lVar2))
     && ((param_4 + param_3 <= lVar2 || (lVar2 != param_3)))) {
                    /* WARNING: Could not recover jumptable at 0xffff0000be05e0bc. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    uVar1 = (**(code **)(param_1 + 0x80))();
    return uVar1;
  }
  return 0;
}


