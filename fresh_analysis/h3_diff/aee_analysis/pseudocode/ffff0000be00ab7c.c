
void FUN_ffff0000be00ab7c(long param_1)

{
  DAT_ffff0000be0f9110 = FUN_ffff0000be05f3d4(param_1 + 1);
  DAT_ffff0000be0f9114 = 1;
  FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
  if (DAT_ffff0000be0f9110 != 0) {
    FUN_ffff0000be05fd98
              (s__s__d__USBDump_timeout_is__u_sec_ffff0000be0b84f5,
               s_cmd_oem_usbdump_timeout_set_ffff0000be0a9f6b,0x203);
    return;
  }
  FUN_ffff0000be05fd98
            (s__s__d__No_USBDump_timeout__but_t_ffff0000be0a8c9f,
             s_cmd_oem_usbdump_timeout_set_ffff0000be0a9f6b,0x205);
  return;
}


