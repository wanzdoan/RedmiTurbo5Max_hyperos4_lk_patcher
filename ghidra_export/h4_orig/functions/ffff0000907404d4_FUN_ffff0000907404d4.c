// ffff0000907404d4  size=40  FUN_ffff0000907404d4

void FUN_ffff0000907404d4(undefined1 *param_1,undefined4 param_2,undefined8 param_3)

{
                    /* WARNING: Could not recover jumptable at 0xffff0000907404f8. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (**(code **)(*(long *)(param_1 + 0x20) + 0x28))
            (*(long *)(param_1 + 0x20),0x30,*param_1,param_2,param_3);
  return;
}


