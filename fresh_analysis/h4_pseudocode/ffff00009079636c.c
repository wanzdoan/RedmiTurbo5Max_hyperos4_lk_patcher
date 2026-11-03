
undefined8 FUN_ffff00009079636c(long param_1,long param_2,long param_3,uint param_4)

{
  ushort uVar1;
  int iVar2;
  char *pcVar3;
  long lVar4;
  
  if (param_1 == 0) {
    pcVar3 = s___s___RPMB_cmd_pointer_NULL_ffff0000907ebb51;
  }
  else if (param_2 == 0) {
    pcVar3 = s___s___RPMB_nonce_buf_pointer_NUL_ffff0000907dba9d;
  }
  else if (param_3 == 0) {
    pcVar3 = s___s___RPMB_ata_buf_pointer_NULL_ffff0000907fe46e;
  }
  else {
    lVar4 = *(long *)(param_1 + 0x28);
    if (lVar4 != 0) {
      uVar1 = *(ushort *)(lVar4 + 0x1fe);
      if ((uint)uVar1 != (((param_4 & 0xff00ff00) >> 8 | (param_4 & 0xff00ff) << 8) & 0xffff)) {
        FUN_ffff00009077e11c
                  (s___s___RPMB_Fail_in_checking_resp_ffff0000907f2222,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea2f5,
                   (uint)(uVar1 >> 8) | (uVar1 & 0xff00ff) << 8);
        return 0xfffffffc;
      }
      uVar1 = *(ushort *)(lVar4 + 0x1fc) >> 8;
      if ((uVar1 & 0x7f) != 0) {
        FUN_ffff00009077e11c
                  (s___s___RPMB_Fail_in_checking_resu_ffff0000907dbb5e,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea2f5,
                   (ulong)(uVar1 & 0x7f) * 0x40 + -0xffff6f7eda9c);
        return 0xfffffffe;
      }
      if ((short)*(ushort *)(lVar4 + 0x1fc) < 0) {
        FUN_ffff00009077e11c
                  (s___s___RPMB_write_counter_is_expi_ffff0000907d8821,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea2f5);
      }
      iVar2 = FUN_ffff00009077eae8(lVar4 + 0x1e4,param_2,0x10);
      if (iVar2 != 0) {
        FUN_ffff00009077e11c
                  (s___s___RPMB_Fail_in_checking_nonc_ffff0000907fffe6,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea2f5);
        return 0xfffffffd;
      }
      iVar2 = FUN_ffff000090796dac(lVar4);
      if (iVar2 != 0) {
        FUN_ffff00009077e11c
                  (s___s___rpmb_get_mac_fail__ret__d_ffff000090801978,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea2f5,iVar2);
        return 0xfffffffa;
      }
      iVar2 = FUN_ffff00009077eae8(lVar4 + 0xc4,DAT_ffff00009090a588,0x20);
      if (iVar2 == 0) {
        FUN_ffff00009077eb20(param_3,lVar4 + 0xe4,0x100);
        return 0;
      }
      FUN_ffff00009077e11c
                (s___s___RPMB_Fail_in_checking_MAC_ffff0000907e219f,
                 s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea2f5);
      FUN_ffff00009077e11c(s_Device_Return_ffff0000907d8847);
      FUN_ffff00009077e11c(s_Host_Recalc_ffff00009080199a);
      return 0xffffffff;
    }
    pcVar3 = s___s___RPMB_pfrm_pointer_NULL_ffff0000907dd515;
  }
  FUN_ffff00009077e11c(pcVar3,s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea2f5);
  return 0xfffffffa;
}


