
void FUN_ffff0000be068f18(undefined8 *param_1,int param_2)

{
  undefined8 *puVar1;
  undefined4 uVar2;
  
  puVar1 = (undefined8 *)*param_1;
  if (puVar1 != (undefined8 *)0x0) {
    *param_1 = 0;
    if (param_2 == 0) {
      uVar2 = *(undefined4 *)(puVar1 + 6);
    }
    else {
      FUN_ffff0000be05fd98
                (s__USB___s___s_FAIL_status___d_ffff0000be0c69ae,
                 s_handle_ept_complete_ffff0000be0a99f4,param_1 + 6,param_2);
      uVar2 = 0;
    }
    FUN_ffff0000be007328(*puVar1,*(undefined4 *)(puVar1 + 1));
    if ((code *)puVar1[2] != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff0000be068f98. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      (*(code *)puVar1[2])(puVar1,uVar2,param_2);
      return;
    }
  }
  return;
}


