
void FUN_ffff00009078cd10(void)

{
  undefined8 *puVar1;
  
  DAT_ffff00009088a21d = DAT_ffff00009088a21d & 0xfe;
  (*(code *)*DAT_ffff00009088a0c0)(DAT_ffff00009088a0c0,2);
  if (DAT_ffff00009088a168 != '\0') {
    FUN_ffff00009078d4c0(&DAT_ffff00009088a128);
    puVar1 = DAT_ffff00009088a128;
    if (DAT_ffff00009088a128 != (undefined8 *)0x0) {
      DAT_ffff00009088a128 = (undefined8 *)0x0;
      FUN_ffff00009077e11c
                (s__USB___s___s_FAIL_status___d_ffff000090803071,
                 s_handle_ept_complete_ffff0000907d86aa,&DAT_ffff00009088a158,0xffffff92);
      FUN_ffff000090707c70(*puVar1,*(undefined4 *)(puVar1 + 1));
      if ((code *)puVar1[2] != (code *)0x0) {
        (*(code *)puVar1[2])(puVar1,0,0xffffff92);
      }
    }
    if (DAT_ffff00009088a1b0 != '\0') {
      FUN_ffff00009078d4c0(&DAT_ffff00009088a170);
      puVar1 = DAT_ffff00009088a170;
      if (DAT_ffff00009088a170 != (undefined8 *)0x0) {
        DAT_ffff00009088a170 = (undefined8 *)0x0;
        FUN_ffff00009077e11c
                  (s__USB___s___s_FAIL_status___d_ffff000090803071,
                   s_handle_ept_complete_ffff0000907d86aa,&DAT_ffff00009088a1a0,0xffffff92);
        FUN_ffff000090707c70(*puVar1,*(undefined4 *)(puVar1 + 1));
        if ((code *)puVar1[2] != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff00009078ce18. Too many branches
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


