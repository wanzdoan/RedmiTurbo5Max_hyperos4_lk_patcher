// File offset entry: 0x90168

undefined8
FUN_02090068(undefined4 *param_1,undefined4 param_2,undefined8 param_3,undefined4 param_4)

{
  undefined8 uVar1;
  
  if (param_1 != (undefined4 *)0x0) {
                    /* WARNING: Could not recover jumptable at 0x02090094. Too many branches */
                    /* WARNING: Treating indirect jump as call */
    uVar1 = (**(code **)(*(long *)(param_1 + 10) + 0x48))
                      (*(long *)(param_1 + 10),0x38,*param_1,param_2,param_3,param_4);
    return uVar1;
  }
  return 0xffffffed;
}


