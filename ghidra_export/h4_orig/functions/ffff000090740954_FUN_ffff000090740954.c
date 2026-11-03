// ffff000090740954  size=512  FUN_ffff000090740954

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090740954(code *param_1)

{
  undefined4 uVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  undefined4 uVar5;
  undefined1 uVar6;
  int iVar7;
  long lVar8;
  undefined8 uVar9;
  uint uVar10;
  ulong uVar11;
  int local_54;
  
  lVar8 = FUN_ffff00009077d514(0xc00);
  uVar5 = _DAT_ffff00001c3401d8;
  uVar4 = _DAT_ffff00001c3400bc;
  uVar3 = _DAT_ffff00001c340074;
  uVar2 = _DAT_ffff00001c340070;
  uVar1 = _DAT_ffff00001c34006c;
  if (lVar8 != 0) {
    FUN_ffff00009077ee50(lVar8,0,0xc00);
    uVar6 = FUN_ffff0000907bbe94(uVar1);
    local_54 = FUN_ffff00009077f6fc
                         (lVar8,0xbff,s_AHB_STATUS__0x_08x_AXI_STATUS__0_ffff0000907daed4,uVar1,
                          uVar5,uVar2,uVar3,uVar4,_DAT_ffff00001c34014c,_DAT_ffff00001c340030,
                          _DAT_ffff00001c340150,_DAT_ffff00001c340034,_DAT_ffff00001c340064,
                          _DAT_ffff00001c340110,uVar6);
    iVar7 = FUN_ffff0000907bbebc(lVar8,&local_54);
    if (iVar7 < 0) {
      FUN_ffff00009077e11c(s__SSPM__bus_tracker_dump_fail_ffff0000907e17b5);
    }
    uVar10 = _DAT_ffff00009086f348;
    iVar7 = FUN_ffff00009077f6fc
                      (lVar8 + local_54,0xbff - (long)local_54,s_TBUF_WPTR__u_ffff000090800d86,
                       _DAT_ffff00009086f348);
    uVar11 = 0;
    local_54 = local_54 + iVar7;
    do {
      uVar10 = uVar10 - 1 & 0xf;
      iVar7 = FUN_ffff00009077f6fc
                        (lVar8 + local_54,(long)(0xbff - local_54),
                         s__u__TBUF__u__T_0x_x_F_0x_x_ffff0000907ff36a,uVar11 & 0xffffffff,uVar10,
                         *(undefined4 *)(uVar11 * 4 + -0xffff6f790c74),
                         *(undefined4 *)(uVar11 * 4 + -0xffff6f790cb4));
      uVar11 = uVar11 + 1;
      local_54 = local_54 + iVar7;
    } while (uVar11 != 0x10);
    if (0xc00 < local_54) {
      FUN_ffff00009077e11c(s__SSPM__data_dump_len__0x_x_>_0x__ffff0000907f9f10,local_54,0xc00);
    }
    uVar9 = FUN_ffff00009077b334(lVar8);
    uVar11 = (*param_1)(uVar9,(long)local_54);
    if ((uVar11 & 1) == 0) {
      FUN_ffff00009077e11c(s__SSPM__AEE_EXPDB_data_fail_ffff0000907d144b);
    }
    thunk_FUN_ffff0000907c314c(lVar8);
    return;
  }
  FUN_ffff00009077e11c(s__SSPM__Can_t_alloc_buffer_ffff0000907e644f);
  return;
}


