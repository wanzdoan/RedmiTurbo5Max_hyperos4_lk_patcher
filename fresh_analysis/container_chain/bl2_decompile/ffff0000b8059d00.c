
undefined8 FUN_ffff0000b8059d00(long param_1,int param_2,uint param_3)

{
  ushort uVar1;
  uint uVar2;
  int iVar3;
  char *pcVar4;
  long lVar5;
  
  if (param_1 == 0) {
    pcVar4 = s___s___RPMB_cmd_pointer_NULL_ffff0000b8094e9f;
  }
  else {
    lVar5 = *(long *)(param_1 + 0x28);
    if (lVar5 != 0) {
      uVar1 = *(ushort *)(lVar5 + 0x1fe);
      if ((uint)uVar1 == (((param_3 & 0xff00ff00) >> 8 | (param_3 & 0xff00ff) << 8) & 0xffff)) {
        uVar1 = *(ushort *)(lVar5 + 0x1fc) >> 8;
        if ((uVar1 & 0x7f) != 0) {
          FUN_ffff0000b804c33c
                    (s___s___RPMB_Fail_in_checking_resu_ffff0000b808bf82,
                     s_ufshcd_authen_data_write_rsp_che_ffff0000b8090516,
                     (ulong)(uVar1 & 0x7f) * 0x40 + -0xffff47f565b4);
          return 0xfffffffe;
        }
        if ((short)*(ushort *)(lVar5 + 0x1fc) < 0) {
          FUN_ffff0000b804c33c
                    (s___s___RPMB_write_counter_is_expi_ffff0000b808a438,
                     s_ufshcd_authen_data_write_rsp_che_ffff0000b8090516);
        }
        uVar2 = FUN_ffff0000b805a190(lVar5);
        if (uVar2 == 0) {
          iVar3 = FUN_ffff0000b804cd08(lVar5 + 0xc4,DAT_ffff0000b81b6948,0x20);
          if (iVar3 != 0) {
            FUN_ffff0000b804c33c
                      (s___s___RPMB_Fail_in_Mac_Check__ffff0000b8087ab4,
                       s_ufshcd_authen_data_write_rsp_che_ffff0000b8090516);
            FUN_ffff0000b804c33c(s_Device_Return_ffff0000b808a416);
            FUN_ffff0000b804c33c(s_Host_Recalc_ffff0000b80a0a68);
            return 0xffffffff;
          }
          uVar2 = (param_2 + 1U & 0xff00ff00) >> 8 | (param_2 + 1U & 0xff00ff) << 8;
          if (*(uint *)(lVar5 + 500) != (uVar2 >> 0x10 | uVar2 << 0x10)) {
            FUN_ffff0000b804c33c
                      (s___s___RPMB_Fail_in_Write_Counter_ffff0000b8092df0,
                       s_ufshcd_authen_data_write_rsp_che_ffff0000b8090516);
            return 0xfffffffb;
          }
          return 0;
        }
        pcVar4 = s___s___rpmb_get_mac_fail__ret__d_ffff0000b80a0a46;
      }
      else {
        uVar2 = (uint)(uVar1 >> 8) | (uVar1 & 0xff00ff) << 8;
        pcVar4 = s___s___RPMB_Fail_in_checking_resp_ffff0000b8098764;
      }
      FUN_ffff0000b804c33c(pcVar4,s_ufshcd_authen_data_write_rsp_che_ffff0000b8090516,uVar2);
      return 0xfffffffa;
    }
    pcVar4 = s___s___RPMB_rpmb_frame_pointer_NU_ffff0000b8099524;
  }
  FUN_ffff0000b804c33c(pcVar4,s_ufshcd_authen_data_write_rsp_che_ffff0000b8090516);
  return 0xfffffffa;
}


