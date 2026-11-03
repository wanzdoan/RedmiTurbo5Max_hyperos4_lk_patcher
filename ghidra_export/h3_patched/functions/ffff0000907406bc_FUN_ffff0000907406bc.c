// ffff0000907406bc  size=512  FUN_ffff0000907406bc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907406bc(code *param_1)

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
  
  lVar8 = FUN_ffff00009077d24c(0xc00);
  uVar5 = _DAT_ffff00001c3401d8;
  uVar4 = _DAT_ffff00001c3400bc;
  uVar3 = _DAT_ffff00001c340074;
  uVar2 = _DAT_ffff00001c340070;
  uVar1 = _DAT_ffff00001c34006c;
  if (lVar8 != 0) {
    FUN_ffff00009077eb88(lVar8,0,0xc00);
    uVar6 = FUN_ffff0000907bb820(uVar1);
    local_54 = FUN_ffff00009077f434
                         (lVar8,0xbff,s_AHB_STATUS__0x_08x_AXI_STATUS__0_ffff0000907dadb2,uVar1,
                          uVar5,uVar2,uVar3,uVar4,_DAT_ffff00001c34014c,_DAT_ffff00001c340030,
                          _DAT_ffff00001c340150,_DAT_ffff00001c340034,_DAT_ffff00001c340064,
                          _DAT_ffff00001c340110,uVar6);
    iVar7 = FUN_ffff0000907bb848(lVar8,&local_54);
    if (iVar7 < 0) {
      FUN_ffff00009077de54(s__SSPM__bus_tracker_dump_fail_ffff0000907e16cd);
    }
    uVar10 = _DAT_ffff00009086f2b0;
    iVar7 = FUN_ffff00009077f434
                      (lVar8 + local_54,0xbff - (long)local_54,s_TBUF_WPTR__u_ffff000090800baa,
                       _DAT_ffff00009086f2b0);
    uVar11 = 0;
    local_54 = local_54 + iVar7;
    do {
      uVar10 = uVar10 - 1 & 0xf;
      iVar7 = FUN_ffff00009077f434
                        (lVar8 + local_54,(long)(0xbff - local_54),
                         s__u__TBUF__u__T_0x_x_F_0x_x_ffff0000907ff1c2,uVar11 & 0xffffffff,uVar10,
                         *(undefined4 *)(uVar11 * 4 + -0xffff6f790d0c),
                         *(undefined4 *)(uVar11 * 4 + -0xffff6f790d4c));
      uVar11 = uVar11 + 1;
      local_54 = local_54 + iVar7;
    } while (uVar11 != 0x10);
    if (0xc00 < local_54) {
      FUN_ffff00009077de54(s__SSPM__data_dump_len__0x_x_>_0x__ffff0000907f9d74,local_54,0xc00);
    }
    uVar9 = FUN_ffff00009077b06c(lVar8);
    uVar11 = (*param_1)(uVar9,(long)local_54);
    if ((uVar11 & 1) == 0) {
      FUN_ffff00009077de54(s__SSPM__AEE_EXPDB_data_fail_ffff0000907d140e);
    }
    thunk_FUN_ffff0000907c2ad8(lVar8);
    return;
  }
  FUN_ffff00009077de54(s__SSPM__Can_t_alloc_buffer_ffff0000907e6324);
  return;
}


