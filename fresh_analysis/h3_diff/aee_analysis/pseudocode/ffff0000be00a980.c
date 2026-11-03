
void FUN_ffff0000be00a980(void)

{
  if (DAT_ffff0000be0f90dc == '\x01') {
    FUN_ffff0000be01b850(&DAT_ffff0000be0f90e0);
    DAT_ffff0000be0f90dc = '\0';
  }
  if (DAT_ffff0000be0f9110 != 0) {
    FUN_ffff0000be01b688(&DAT_ffff0000be0f90e0);
    DAT_ffff0000be0f9110 = DAT_ffff0000be0f9110 * 1000;
    DAT_ffff0000be0f90dc = 1;
    FUN_ffff0000be01b830(&DAT_ffff0000be0f90e0,DAT_ffff0000be0f9110,FUN_ffff0000be00a950,0);
    FUN_ffff0000be05fd98
              (s__s__d__setup_usbdump_timeout_tim_ffff0000be0c1811,
               s_setup_usbdump_timer_ffff0000be0a9f57,0x1c0,(ulong)DAT_ffff0000be0f9110 / 1000);
    return;
  }
  FUN_ffff0000be05fd98
            (s__s__d__No_USBDump_timeout__done_ffff0000be0b732d,
             s_setup_usbdump_timer_ffff0000be0a9f57,0x1b0);
  return;
}


