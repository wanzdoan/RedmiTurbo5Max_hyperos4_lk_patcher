// ffff00009077bdf4  size=68  FUN_ffff00009077bdf4

undefined8 FUN_ffff00009077bdf4(long param_1,undefined8 param_2,long param_3,long param_4)

{
  undefined8 uVar1;
  long lVar2;
  
  if ((((-1 < param_3) && (param_4 != 0)) && (lVar2 = *(long *)(param_1 + 0x20), param_3 < lVar2))
     && ((param_4 + param_3 <= lVar2 || (lVar2 != param_3)))) {
                    /* WARNING: Could not recover jumptable at 0xffff00009077be28. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    uVar1 = (**(code **)(param_1 + 0x70))();
    return uVar1;
  }
  return 0;
}


