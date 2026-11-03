
void FUN_ffff0000be06a89c(void)

{
  undefined8 *puVar1;
  
  DAT_ffff0000be1348a5 = DAT_ffff0000be1348a5 & 0xfe;
  (*(code *)*DAT_ffff0000be134748)(DAT_ffff0000be134748,2);
  if (DAT_ffff0000be1347f0 != '\0') {
    FUN_ffff0000be06b04c(&DAT_ffff0000be1347b0);
    puVar1 = DAT_ffff0000be1347b0;
    if (DAT_ffff0000be1347b0 != (undefined8 *)0x0) {
      DAT_ffff0000be1347b0 = (undefined8 *)0x0;
      FUN_ffff0000be05fd98
                (s__USB___s___s_FAIL_status___d_ffff0000be0c69ae,
                 s_handle_ept_complete_ffff0000be0a99f4,&DAT_ffff0000be1347e0,0xffffff92);
      FUN_ffff0000be007328(*puVar1,*(undefined4 *)(puVar1 + 1));
      if ((code *)puVar1[2] != (code *)0x0) {
        (*(code *)puVar1[2])(puVar1,0,0xffffff92);
      }
    }
    if (DAT_ffff0000be134838 != '\0') {
      FUN_ffff0000be06b04c(&DAT_ffff0000be1347f8);
      puVar1 = DAT_ffff0000be1347f8;
      if (DAT_ffff0000be1347f8 != (undefined8 *)0x0) {
        DAT_ffff0000be1347f8 = (undefined8 *)0x0;
        FUN_ffff0000be05fd98
                  (s__USB___s___s_FAIL_status___d_ffff0000be0c69ae,
                   s_handle_ept_complete_ffff0000be0a99f4,&DAT_ffff0000be134828,0xffffff92);
        FUN_ffff0000be007328(*puVar1,*(undefined4 *)(puVar1 + 1));
        if ((code *)puVar1[2] != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff0000be06a9a4. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
          (*(code *)puVar1[2])(puVar1,0,0xffffff92);
          return;
        }
      }
    }
  }
  return;
}


