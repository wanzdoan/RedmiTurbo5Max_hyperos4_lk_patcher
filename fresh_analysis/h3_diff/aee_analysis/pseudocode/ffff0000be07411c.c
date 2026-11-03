
undefined8 FUN_ffff0000be07411c(long param_1,int param_2,uint param_3)

{
  ushort uVar1;
  uint uVar2;
  int iVar3;
  char *pcVar4;
  long lVar5;
  
  if (param_1 == 0) {
    pcVar4 = s___s___RPMB_cmd_pointer_NULL_ffff0000be0b7047;
  }
  else {
    lVar5 = *(long *)(param_1 + 0x28);
    if (lVar5 != 0) {
      uVar1 = *(ushort *)(lVar5 + 0x1fe);
      if ((uint)uVar1 == (((param_3 & 0xff00ff00) >> 8 | (param_3 & 0xff00ff) << 8) & 0xffff)) {
        uVar1 = *(ushort *)(lVar5 + 0x1fc) >> 8;
        if ((uVar1 & 0x7f) != 0) {
          FUN_ffff0000be05fd98
                    (s___s___RPMB_Fail_in_checking_resu_ffff0000be0abfb0,
                     s_ufshcd_authen_data_write_rsp_che_ffff0000be0b16f6,
                     (ulong)(uVar1 & 0x7f) * 0x40 + -0xffff41f2d9d4);
          return 0xfffffffe;
        }
        if ((short)*(ushort *)(lVar5 + 0x1fc) < 0) {
          FUN_ffff0000be05fd98
                    (s___s___RPMB_write_counter_is_expi_ffff0000be0a9b6b,
                     s_ufshcd_authen_data_write_rsp_che_ffff0000be0b16f6);
        }
        uVar2 = FUN_ffff0000be074520(lVar5);
        if (uVar2 == 0) {
          iVar3 = FUN_ffff0000be060778(lVar5 + 0xc4,DAT_ffff0000be1b4c08,0x20);
          if (iVar3 != 0) {
            FUN_ffff0000be05fd98
                      (s___s___RPMB_Fail_in_Mac_Check__ffff0000be0a6513,
                       s_ufshcd_authen_data_write_rsp_che_ffff0000be0b16f6);
            FUN_ffff0000be05fd98(s_Device_Return_ffff0000be0a9b49);
            FUN_ffff0000be05fd98(s_Host_Recalc_ffff0000be0c5ae8);
            return 0xffffffff;
          }
          uVar2 = (param_2 + 1U & 0xff00ff00) >> 8 | (param_2 + 1U & 0xff00ff) << 8;
          if (*(uint *)(lVar5 + 500) != (uVar2 >> 0x10 | uVar2 << 0x10)) {
            FUN_ffff0000be05fd98
                      (s___s___RPMB_Fail_in_Write_Counter_ffff0000be0b4b0d,
                       s_ufshcd_authen_data_write_rsp_che_ffff0000be0b16f6);
            return 0xfffffffb;
          }
          return 0;
        }
        pcVar4 = s___s___rpmb_get_mac_fail__ret__d_ffff0000be0c5ac6;
      }
      else {
        uVar2 = (uint)(uVar1 >> 8) | (uVar1 & 0xff00ff) << 8;
        pcVar4 = s___s___RPMB_Fail_in_checking_resp_ffff0000be0bb7b9;
      }
      FUN_ffff0000be05fd98(pcVar4,s_ufshcd_authen_data_write_rsp_che_ffff0000be0b16f6,uVar2);
      return 0xfffffffa;
    }
    pcVar4 = s___s___RPMB_rpmb_frame_pointer_NU_ffff0000be0bc8a1;
  }
  FUN_ffff0000be05fd98(pcVar4,s_ufshcd_authen_data_write_rsp_che_ffff0000be0b16f6);
  return 0xfffffffa;
}


