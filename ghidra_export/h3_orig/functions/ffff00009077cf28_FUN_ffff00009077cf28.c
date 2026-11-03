// ffff00009077cf28  size=20  FUN_ffff00009077cf28

void FUN_ffff00009077cf28(undefined8 *param_1,undefined8 param_2)

{
  if (param_1 != (undefined8 *)0x0) {
    *param_1 = 0;
    param_1[1] = 0;
    param_1[2] = param_2;
    param_1[3] = 0;
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04(s_lib_dpc_dpc_c_ffff0000907ed05a,0x24,&DAT_ffff0000907deb79);
}


