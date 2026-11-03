// ffff0000907960ac  size=448  FUN_ffff0000907960ac

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907960ac(long param_1,long param_2,long param_3,uint param_4)

{
  ushort uVar1;
  int iVar2;
  char *pcVar3;
  long lVar4;
  
  if (param_1 == 0) {
    pcVar3 = s___s___RPMB_cmd_pointer_NULL_ffff0000907eb9ce;
  }
  else if (param_2 == 0) {
    pcVar3 = s___s___RPMB_nonce_buf_pointer_NUL_ffff0000907db992;
  }
  else if (param_3 == 0) {
    pcVar3 = s___s___RPMB_ata_buf_pointer_NULL_ffff0000907fe2c6;
  }
  else {
    lVar4 = *(long *)(param_1 + 0x28);
    if (lVar4 != 0) {
      uVar1 = *(ushort *)(lVar4 + 0x1fe);
      if ((uint)uVar1 != (((param_4 & 0xff00ff00) >> 8 | (param_4 & 0xff00ff) << 8) & 0xffff)) {
        FUN_ffff00009077de54
                  (s___s___RPMB_Fail_in_checking_resp_ffff0000907f2096,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea1b1,
                   (uint)(uVar1 >> 8) | (uVar1 & 0xff00ff) << 8);
        return 0xfffffffc;
      }
      uVar1 = *(ushort *)(lVar4 + 0x1fc) >> 8;
      if ((uVar1 & 0x7f) != 0) {
        FUN_ffff00009077de54
                  (s___s___RPMB_Fail_in_checking_resu_ffff0000907dba53,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea1b1,
                   (ulong)(uVar1 & 0x7f) * 0x40 + -0xffff6f7edcc4);
        return 0xfffffffe;
      }
      if ((short)*(ushort *)(lVar4 + 0x1fc) < 0) {
        FUN_ffff00009077de54
                  (s___s___RPMB_write_counter_is_expi_ffff0000907d878b,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea1b1);
      }
      iVar2 = FUN_ffff00009077e820(lVar4 + 0x1e4,param_2,0x10);
      if (iVar2 != 0) {
        FUN_ffff00009077de54
                  (s___s___RPMB_Fail_in_checking_nonc_ffff0000907ffe3e,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea1b1);
        return 0xfffffffd;
      }
      iVar2 = FUN_ffff000090796aec(lVar4);
      if (iVar2 != 0) {
        FUN_ffff00009077de54
                  (s___s___rpmb_get_mac_fail__ret__d_ffff00009080179c,
                   s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea1b1,iVar2);
        return 0xfffffffa;
      }
      iVar2 = FUN_ffff00009077e820(lVar4 + 0xc4,_DAT_ffff00009090a4c8,0x20);
      if (iVar2 == 0) {
        FUN_ffff00009077e858(param_3,lVar4 + 0xe4,0x100);
        return 0;
      }
      FUN_ffff00009077de54
                (s___s___RPMB_Fail_in_checking_MAC_ffff0000907e20ce,
                 s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea1b1);
      FUN_ffff00009077de54(s_Device_Return_ffff0000907d87b1);
      FUN_ffff00009077de54(s_Host_Recalc_ffff0000908017be);
      return 0xffffffff;
    }
    pcVar3 = s___s___RPMB_pfrm_pointer_NULL_ffff0000907dd472;
  }
  FUN_ffff00009077de54(pcVar3,s_ufshcd_authen_data_read_rsp_chec_ffff0000907ea1b1);
  return 0xfffffffa;
}


