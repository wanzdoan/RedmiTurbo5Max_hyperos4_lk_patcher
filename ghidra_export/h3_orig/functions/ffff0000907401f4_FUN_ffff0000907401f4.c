// ffff0000907401f4  size=40  FUN_ffff0000907401f4

void FUN_ffff0000907401f4(undefined1 *param_1,undefined4 param_2,undefined8 param_3)

{
                    /* WARNING: Could not recover jumptable at 0xffff000090740218. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (**(code **)(*(long *)(param_1 + 0x20) + 0x20))
            (*(long *)(param_1 + 0x20),0x38,*param_1,param_2,param_3);
  return;
}


