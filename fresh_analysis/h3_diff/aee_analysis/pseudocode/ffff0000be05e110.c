
undefined8 FUN_ffff0000be05e110(long param_1,long param_2,long param_3)

{
  undefined8 uVar1;
  long lVar2;
  
  if ((((-1 < param_2) && (param_3 != 0)) && (lVar2 = *(long *)(param_1 + 0x20), param_2 < lVar2))
     && ((param_3 + param_2 <= lVar2 || (param_3 = lVar2 - param_2, param_3 != 0)))) {
                    /* WARNING: Could not recover jumptable at 0xffff0000be05e144. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    uVar1 = (**(code **)(param_1 + 0x90))(param_1,param_2,param_3);
    return uVar1;
  }
  return 0;
}


