// ffff00009078ca50  size=284  FUN_ffff00009078ca50

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009078ca50(void)

{
  undefined8 *puVar1;
  
  DAT_ffff00009088a185 = DAT_ffff00009088a185 & 0xfe;
  (*(code *)*_DAT_ffff00009088a028)(_DAT_ffff00009088a028,2);
  if (DAT_ffff00009088a0d0 != '\0') {
    FUN_ffff00009078d200(&DAT_ffff00009088a090);
    puVar1 = _DAT_ffff00009088a090;
    if (_DAT_ffff00009088a090 != (undefined8 *)0x0) {
      _DAT_ffff00009088a090 = (undefined8 *)0x0;
      FUN_ffff00009077de54
                (s__USB___s___s_FAIL_status___d_ffff000090802e6d,
                 s_handle_ept_complete_ffff0000907d8614,0xffff00009088a0c0,0xffffff92);
      FUN_ffff000090707ce4(*puVar1,*(undefined4 *)(puVar1 + 1));
      if ((code *)puVar1[2] != (code *)0x0) {
        (*(code *)puVar1[2])(puVar1,0,0xffffff92);
      }
    }
    if (DAT_ffff00009088a118 != '\0') {
      FUN_ffff00009078d200(&DAT_ffff00009088a0d8);
      puVar1 = _DAT_ffff00009088a0d8;
      if (_DAT_ffff00009088a0d8 != (undefined8 *)0x0) {
        _DAT_ffff00009088a0d8 = (undefined8 *)0x0;
        FUN_ffff00009077de54
                  (s__USB___s___s_FAIL_status___d_ffff000090802e6d,
                   s_handle_ept_complete_ffff0000907d8614,0xffff00009088a108,0xffffff92);
        FUN_ffff000090707ce4(*puVar1,*(undefined4 *)(puVar1 + 1));
        if ((code *)puVar1[2] != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff00009078cb58. Too many branches
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


