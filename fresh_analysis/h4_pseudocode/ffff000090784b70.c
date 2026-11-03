
void FUN_ffff000090784b70
               (undefined8 param_1,undefined8 param_2,undefined4 param_3,undefined4 param_4,
               int param_5,int param_6,uint param_7,uint param_8)

{
  int iVar1;
  long lVar2;
  undefined4 uVar3;
  byte local_34 [4];
  
  lVar2 = FUN_ffff000090740610();
  local_34[0] = 0;
  if (lVar2 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_clkbuf__ffff0000907f8e39,0x54,&DAT_ffff0000907f556d);
  }
  iVar1 = FUN_ffff00009074046c(lVar2,param_3,local_34,1);
  if (iVar1 < 0) {
    uVar3 = 0;
  }
  else {
    local_34[0] = local_34[0] & ((byte)(param_5 << (ulong)(param_7 & 0x1f)) ^ 0xff);
    uVar3 = 1;
    iVar1 = FUN_ffff0000907404b4(lVar2,param_3,local_34,1);
    if (-1 < iVar1) {
      iVar1 = FUN_ffff00009074046c(lVar2,param_4,local_34,1);
      if (iVar1 < 0) {
        uVar3 = 2;
      }
      else {
        local_34[0] = local_34[0] & ((byte)(param_6 << (ulong)(param_8 & 0x1f)) ^ 0xff);
        iVar1 = FUN_ffff0000907404b4(lVar2,param_4,local_34,1);
        if (-1 < iVar1) {
          return;
        }
        uVar3 = 3;
      }
    }
  }
  FUN_ffff00009077e11c
            (s___s__SPMI_R_W_FAIL_with_ret____d_ffff0000907f37af,
             s_clk_buf_mask_voter_ffff0000907d9de2,iVar1,uVar3);
  return;
}


