// File offset entry: 0x901a0

undefined8
FUN_020900a0(undefined4 *param_1,undefined4 param_2,undefined8 param_3,undefined4 param_4)

{
  undefined8 uVar1;
  
  if (param_1 != (undefined4 *)0x0) {
                    /* WARNING: Could not recover jumptable at 0x020900cc. Too many branches */
                    /* WARNING: Treating indirect jump as call */
    uVar1 = (**(code **)(*(long *)(param_1 + 10) + 0x50))
                      (*(long *)(param_1 + 10),0x30,*param_1,param_2,param_3,param_4);
    return uVar1;
  }
  return 0xffffffed;
}


