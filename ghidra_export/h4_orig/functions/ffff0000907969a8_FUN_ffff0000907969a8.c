// ffff0000907969a8  size=372  FUN_ffff0000907969a8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907969a8(long param_1,int param_2,uint param_3)

{
  ushort uVar1;
  uint uVar2;
  int iVar3;
  char *pcVar4;
  long lVar5;
  
  if (param_1 == 0) {
    pcVar4 = s___s___RPMB_cmd_pointer_NULL_ffff0000907ebb51;
  }
  else {
    lVar5 = *(long *)(param_1 + 0x28);
    if (lVar5 != 0) {
      uVar1 = *(ushort *)(lVar5 + 0x1fe);
      if ((uint)uVar1 == (((param_3 & 0xff00ff00) >> 8 | (param_3 & 0xff00ff) << 8) & 0xffff)) {
        uVar1 = *(ushort *)(lVar5 + 0x1fc) >> 8;
        if ((uVar1 & 0x7f) != 0) {
          FUN_ffff00009077e11c
                    (s___s___RPMB_Fail_in_checking_resu_ffff0000907dbb5e,
                     s_ufshcd_authen_data_write_rsp_che_ffff0000907e3d26,
                     (ulong)(uVar1 & 0x7f) * 0x40 + -0xffff6f7eda9c);
          return 0xfffffffe;
        }
        if ((short)*(ushort *)(lVar5 + 0x1fc) < 0) {
          FUN_ffff00009077e11c
                    (s___s___RPMB_write_counter_is_expi_ffff0000907d8821,
                     s_ufshcd_authen_data_write_rsp_che_ffff0000907e3d26);
        }
        uVar2 = FUN_ffff000090796dac(lVar5);
        if (uVar2 == 0) {
          iVar3 = FUN_ffff00009077eae8(lVar5 + 0xc4,_DAT_ffff00009090a588,0x20);
          if (iVar3 != 0) {
            FUN_ffff00009077e11c
                      (s___s___RPMB_Fail_in_Mac_Check__ffff0000907d3a64,
                       s_ufshcd_authen_data_write_rsp_che_ffff0000907e3d26);
            FUN_ffff00009077e11c(s_Device_Return_ffff0000907d87ff);
            FUN_ffff00009077e11c(s_Host_Recalc_ffff00009080199a);
            return 0xffffffff;
          }
          uVar2 = (param_2 + 1U & 0xff00ff00) >> 8 | (param_2 + 1U & 0xff00ff) << 8;
          if (*(uint *)(lVar5 + 500) != (uVar2 >> 0x10 | uVar2 << 0x10)) {
            FUN_ffff00009077e11c
                      (s___s___RPMB_Fail_in_Write_Counter_ffff0000907e88ae,
                       s_ufshcd_authen_data_write_rsp_che_ffff0000907e3d26);
            return 0xfffffffb;
          }
          return 0;
        }
        pcVar4 = s___s___rpmb_get_mac_fail__ret__d_ffff000090801978;
      }
      else {
        uVar2 = (uint)(uVar1 >> 8) | (uVar1 & 0xff00ff) << 8;
        pcVar4 = s___s___RPMB_Fail_in_checking_resp_ffff0000907f2222;
      }
      FUN_ffff00009077e11c(pcVar4,s_ufshcd_authen_data_write_rsp_che_ffff0000907e3d26,uVar2);
      return 0xfffffffa;
    }
    pcVar4 = s___s___RPMB_rpmb_frame_pointer_NU_ffff0000907f3a2f;
  }
  FUN_ffff00009077e11c(pcVar4,s_ufshcd_authen_data_write_rsp_che_ffff0000907e3d26);
  return 0xfffffffa;
}


