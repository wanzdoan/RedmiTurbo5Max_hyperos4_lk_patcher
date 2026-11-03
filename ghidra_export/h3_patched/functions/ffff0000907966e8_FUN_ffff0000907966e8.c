// ffff0000907966e8  size=372  FUN_ffff0000907966e8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907966e8(long param_1,int param_2,uint param_3)

{
  ushort uVar1;
  uint uVar2;
  int iVar3;
  char *pcVar4;
  long lVar5;
  
  if (param_1 == 0) {
    pcVar4 = s___s___RPMB_cmd_pointer_NULL_ffff0000907eb9b7;
  }
  else {
    lVar5 = *(long *)(param_1 + 0x28);
    if (lVar5 != 0) {
      uVar1 = *(ushort *)(lVar5 + 0x1fe);
      if ((uint)uVar1 == (((param_3 & 0xff00ff00) >> 8 | (param_3 & 0xff00ff) << 8) & 0xffff)) {
        uVar1 = *(ushort *)(lVar5 + 0x1fc) >> 8;
        if ((uVar1 & 0x7f) != 0) {
          FUN_ffff00009077de54
                    (s___s___RPMB_Fail_in_checking_resu_ffff0000907dba3c,
                     s_ufshcd_authen_data_write_rsp_che_ffff0000907e3c3e,
                     (ulong)(uVar1 & 0x7f) * 0x40 + -0xffff6f7edcc4);
          return 0xfffffffe;
        }
        if ((short)*(ushort *)(lVar5 + 0x1fc) < 0) {
          FUN_ffff00009077de54
                    (s___s___RPMB_write_counter_is_expi_ffff0000907d878b,
                     s_ufshcd_authen_data_write_rsp_che_ffff0000907e3c3e);
        }
        uVar2 = FUN_ffff000090796aec(lVar5);
        if (uVar2 == 0) {
          iVar3 = FUN_ffff00009077e820(lVar5 + 0xc4,_DAT_ffff00009090a4c8,0x20);
          if (iVar3 != 0) {
            FUN_ffff00009077de54
                      (s___s___RPMB_Fail_in_Mac_Check__ffff0000907d3a27,
                       s_ufshcd_authen_data_write_rsp_che_ffff0000907e3c3e);
            FUN_ffff00009077de54(s_Device_Return_ffff0000907d8769);
            FUN_ffff00009077de54(s_Host_Recalc_ffff0000908017be);
            return 0xffffffff;
          }
          uVar2 = (param_2 + 1U & 0xff00ff00) >> 8 | (param_2 + 1U & 0xff00ff) << 8;
          if (*(uint *)(lVar5 + 500) != (uVar2 >> 0x10 | uVar2 << 0x10)) {
            FUN_ffff00009077de54
                      (s___s___RPMB_Fail_in_Write_Counter_ffff0000907e8753,
                       s_ufshcd_authen_data_write_rsp_che_ffff0000907e3c3e);
            return 0xfffffffb;
          }
          return 0;
        }
        pcVar4 = s___s___rpmb_get_mac_fail__ret__d_ffff00009080179c;
      }
      else {
        uVar2 = (uint)(uVar1 >> 8) | (uVar1 & 0xff00ff) << 8;
        pcVar4 = s___s___RPMB_Fail_in_checking_resp_ffff0000907f207f;
      }
      FUN_ffff00009077de54(pcVar4,s_ufshcd_authen_data_write_rsp_che_ffff0000907e3c3e,uVar2);
      return 0xfffffffa;
    }
    pcVar4 = s___s___RPMB_rpmb_frame_pointer_NU_ffff0000907f388c;
  }
  FUN_ffff00009077de54(pcVar4,s_ufshcd_authen_data_write_rsp_che_ffff0000907e3c3e);
  return 0xfffffffa;
}


