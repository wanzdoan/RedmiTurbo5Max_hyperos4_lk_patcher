
undefined8
FUN_ffff00009074046c(undefined1 *param_1,undefined4 param_2,undefined8 param_3,int param_4)

{
  undefined8 uVar1;
  
  if ((param_4 - 0x11U & 0xff) < 0xf0) {
    return 0xfffffff8;
  }
                    /* WARNING: Could not recover jumptable at 0xffff0000907404b0. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  uVar1 = (**(code **)(*(long *)(param_1 + 0x20) + 0x20))
                    (*(long *)(param_1 + 0x20),0x38,*param_1,param_2,param_3,param_4);
  return uVar1;
}


