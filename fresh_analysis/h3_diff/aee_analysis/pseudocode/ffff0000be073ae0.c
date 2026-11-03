
undefined8 FUN_ffff0000be073ae0(long param_1,long param_2,long param_3,uint param_4)

{
  ushort uVar1;
  int iVar2;
  char *pcVar3;
  long lVar4;
  
  if (param_1 == 0) {
    pcVar3 = s___s___RPMB_cmd_pointer_NULL_ffff0000be0b7047;
  }
  else if (param_2 == 0) {
    pcVar3 = s___s___RPMB_nonce_buf_pointer_NUL_ffff0000be0abeef;
  }
  else if (param_3 == 0) {
    pcVar3 = s___s___RPMB_ata_buf_pointer_NULL_ffff0000be0c37ef;
  }
  else {
    lVar4 = *(long *)(param_1 + 0x28);
    if (lVar4 != 0) {
      uVar1 = *(ushort *)(lVar4 + 0x1fe);
      if ((uint)uVar1 != (((param_4 & 0xff00ff00) >> 8 | (param_4 & 0xff00ff) << 8) & 0xffff)) {
        FUN_ffff0000be05fd98
                  (s___s___RPMB_Fail_in_checking_resp_ffff0000be0bb7b9,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000be0b5d9a,
                   (uint)(uVar1 >> 8) | (uVar1 & 0xff00ff) << 8);
        return 0xfffffffc;
      }
      uVar1 = *(ushort *)(lVar4 + 0x1fc) >> 8;
      if ((uVar1 & 0x7f) != 0) {
        FUN_ffff0000be05fd98
                  (s___s___RPMB_Fail_in_checking_resu_ffff0000be0abfb0,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000be0b5d9a,
                   (ulong)(uVar1 & 0x7f) * 0x40 + -0xffff41f2d9d4);
        return 0xfffffffe;
      }
      if ((short)*(ushort *)(lVar4 + 0x1fc) < 0) {
        FUN_ffff0000be05fd98
                  (s___s___RPMB_write_counter_is_expi_ffff0000be0a9b6b,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000be0b5d9a);
      }
      iVar2 = FUN_ffff0000be060778(lVar4 + 0x1e4,param_2,0x10);
      if (iVar2 != 0) {
        FUN_ffff0000be05fd98
                  (s___s___RPMB_Fail_in_checking_nonc_ffff0000be0c4a0b,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000be0b5d9a);
        return 0xfffffffd;
      }
      iVar2 = FUN_ffff0000be074520(lVar4);
      if (iVar2 != 0) {
        FUN_ffff0000be05fd98
                  (s___s___rpmb_get_mac_fail__ret__d_ffff0000be0c5ac6,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000be0b5d9a,iVar2);
        return 0xfffffffa;
      }
      iVar2 = FUN_ffff0000be060778(lVar4 + 0xc4,DAT_ffff0000be1b4c08,0x20);
      if (iVar2 == 0) {
        FUN_ffff0000be0607b0(param_3,lVar4 + 0xe4,0x100);
        return 0;
      }
      FUN_ffff0000be05fd98
                (s___s___RPMB_Fail_in_checking_MAC_ffff0000be0b0619,
                 s_ufshcd_authen_data_read_rsp_chec_ffff0000be0b5d9a);
      FUN_ffff0000be05fd98(s_Device_Return_ffff0000be0a9b91);
      FUN_ffff0000be05fd98(s_Host_Recalc_ffff0000be0c5ae8);
      return 0xffffffff;
    }
    pcVar3 = s___s___RPMB_pfrm_pointer_NULL_ffff0000be0ad049;
  }
  FUN_ffff0000be05fd98(pcVar3,s_ufshcd_authen_data_read_rsp_chec_ffff0000be0b5d9a);
  return 0xfffffffa;
}


