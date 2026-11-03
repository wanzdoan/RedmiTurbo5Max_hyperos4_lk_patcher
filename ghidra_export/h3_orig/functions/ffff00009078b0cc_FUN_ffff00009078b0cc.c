// ffff00009078b0cc  size=152  FUN_ffff00009078b0cc

void FUN_ffff00009078b0cc(undefined8 *param_1,int param_2)

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
      FUN_ffff00009077de54
                (s__USB___s___s_FAIL_status___d_ffff000090802e6d,
                 s_handle_ept_complete_ffff0000907d8614,param_1 + 6,param_2);
      uVar2 = 0;
    }
    FUN_ffff000090707ce4(*puVar1,*(undefined4 *)(puVar1 + 1));
    if ((code *)puVar1[2] != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff00009078b14c. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      (*(code *)puVar1[2])(puVar1,uVar2,param_2);
      return;
    }
  }
  return;
}


