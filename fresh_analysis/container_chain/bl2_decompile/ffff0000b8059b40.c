
undefined8 FUN_ffff0000b8059b40(long param_1,long param_2,long param_3,uint param_4)

{
  ushort uVar1;
  int iVar2;
  char *pcVar3;
  long lVar4;
  
  if (param_1 == 0) {
    pcVar3 = s___s___RPMB_cmd_pointer_NULL_ffff0000b8094e9f;
  }
  else if (param_2 == 0) {
    pcVar3 = s___s___RPMB_nonce_buf_pointer_NUL_ffff0000b808bec1;
  }
  else if (param_3 == 0) {
    pcVar3 = s___s___RPMB_ata_buf_pointer_NULL_ffff0000b809ef6f;
  }
  else {
    lVar4 = *(long *)(param_1 + 0x28);
    if (lVar4 != 0) {
      uVar1 = *(ushort *)(lVar4 + 0x1fe);
      if ((uint)uVar1 != (((param_4 & 0xff00ff00) >> 8 | (param_4 & 0xff00ff) << 8) & 0xffff)) {
        FUN_ffff0000b804c33c
                  (s___s___RPMB_Fail_in_checking_resp_ffff0000b8098764,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000b8093e56,
                   (uint)(uVar1 >> 8) | (uVar1 & 0xff00ff) << 8);
        return 0xfffffffc;
      }
      uVar1 = *(ushort *)(lVar4 + 0x1fc) >> 8;
      if ((uVar1 & 0x7f) != 0) {
        FUN_ffff0000b804c33c
                  (s___s___RPMB_Fail_in_checking_resu_ffff0000b808bf82,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000b8093e56,
                   (ulong)(uVar1 & 0x7f) * 0x40 + -0xffff47f565b4);
        return 0xfffffffe;
      }
      if ((short)*(ushort *)(lVar4 + 0x1fc) < 0) {
        FUN_ffff0000b804c33c
                  (s___s___RPMB_write_counter_is_expi_ffff0000b808a438,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000b8093e56);
      }
      iVar2 = FUN_ffff0000b804cd08(lVar4 + 0x1e4,param_2,0x10);
      if (iVar2 != 0) {
        FUN_ffff0000b804c33c
                  (s___s___RPMB_Fail_in_checking_nonc_ffff0000b809fe7e,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000b8093e56);
        return 0xfffffffd;
      }
      iVar2 = FUN_ffff0000b805a190(lVar4);
      if (iVar2 != 0) {
        FUN_ffff0000b804c33c
                  (s___s___rpmb_get_mac_fail__ret__d_ffff0000b80a0a46,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000b8093e56,iVar2);
        return 0xfffffffa;
      }
      iVar2 = FUN_ffff0000b804cd08(lVar4 + 0xc4,DAT_ffff0000b81b6948,0x20);
      if (iVar2 == 0) {
        FUN_ffff0000b804cd40(param_3,lVar4 + 0xe4,0x100);
        return 0;
      }
      FUN_ffff0000b804c33c
                (s___s___RPMB_Fail_in_checking_MAC_ffff0000b808f661,
                 s_ufshcd_authen_data_read_rsp_chec_ffff0000b8093e56);
      FUN_ffff0000b804c33c(s_Device_Return_ffff0000b808a45e);
      FUN_ffff0000b804c33c(s_Host_Recalc_ffff0000b80a0a68);
      return 0xffffffff;
    }
    pcVar3 = s___s___RPMB_pfrm_pointer_NULL_ffff0000b808cbec;
  }
  FUN_ffff0000b804c33c(pcVar3,s_ufshcd_authen_data_read_rsp_chec_ffff0000b8093e56);
  return 0xfffffffa;
}


