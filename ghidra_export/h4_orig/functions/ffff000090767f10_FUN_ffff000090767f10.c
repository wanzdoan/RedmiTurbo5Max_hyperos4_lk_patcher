// ffff000090767f10  size=376  FUN_ffff000090767f10

void FUN_ffff000090767f10(void)

{
  long lVar1;
  char cVar2;
  byte bVar3;
  int iVar4;
  char *pcVar5;
  long lVar6;
  byte *pbVar7;
  char cVar8;
  char local_100 [40];
  char local_d8 [40];
  byte local_b0 [32];
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  undefined8 local_20;
  ulong uStack_18;
  
  local_20 = 0;
  uStack_18 = 0;
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_b0[0x10] = 0;
  local_b0[0x11] = 0;
  local_b0[0x12] = 0;
  local_b0[0x13] = 0;
  local_b0[0x14] = 0;
  local_b0[0x15] = 0;
  local_b0[0x16] = 0;
  local_b0[0x17] = 0;
  local_b0[0x18] = 0;
  local_b0[0x19] = 0;
  local_b0[0x1a] = 0;
  local_b0[0x1b] = 0;
  local_b0[0x1c] = 0;
  local_b0[0x1d] = 0;
  local_b0[0x1e] = 0;
  local_b0[0x1f] = 0;
  local_b0[0] = 0;
  local_b0[1] = 0;
  local_b0[2] = 0;
  local_b0[3] = 0;
  local_b0[4] = 0;
  local_b0[5] = 0;
  local_b0[6] = 0;
  local_b0[7] = 0;
  local_b0[8] = 0;
  local_b0[9] = 0;
  local_b0[10] = 0;
  local_b0[0xb] = 0;
  local_b0[0xc] = 0;
  local_b0[0xd] = 0;
  local_b0[0xe] = 0;
  local_b0[0xf] = 0;
  local_d8[0x10] = '\0';
  local_d8[0x11] = '\0';
  local_d8[0x12] = '\0';
  local_d8[0x13] = '\0';
  local_d8[0x14] = '\0';
  local_d8[0x15] = '\0';
  local_d8[0x16] = '\0';
  local_d8[0x17] = '\0';
  local_d8[0x18] = '\0';
  local_d8[0x19] = '\0';
  local_d8[0x1a] = '\0';
  local_d8[0x1b] = '\0';
  local_d8[0x1c] = '\0';
  local_d8[0x1d] = '\0';
  local_d8[0x1e] = '\0';
  local_d8[0x1f] = '\0';
  local_d8[0] = '\0';
  local_d8[1] = '\0';
  local_d8[2] = '\0';
  local_d8[3] = '\0';
  local_d8[4] = '\0';
  local_d8[5] = '\0';
  local_d8[6] = '\0';
  local_d8[7] = '\0';
  local_d8[8] = '\0';
  local_d8[9] = '\0';
  local_d8[10] = '\0';
  local_d8[0xb] = '\0';
  local_d8[0xc] = '\0';
  local_d8[0xd] = '\0';
  local_d8[0xe] = '\0';
  local_d8[0xf] = '\0';
  local_100[0x10] = '\0';
  local_100[0x11] = '\0';
  local_100[0x12] = '\0';
  local_100[0x13] = '\0';
  local_100[0x14] = '\0';
  local_100[0x15] = '\0';
  local_100[0x16] = '\0';
  local_100[0x17] = '\0';
  local_100[0x18] = '\0';
  local_100[0x19] = '\0';
  local_100[0x1a] = '\0';
  local_100[0x1b] = '\0';
  local_100[0x1c] = '\0';
  local_100[0x1d] = '\0';
  local_100[0x1e] = '\0';
  local_100[0x1f] = '\0';
  local_100[0] = '\0';
  local_100[1] = '\0';
  local_100[2] = '\0';
  local_100[3] = '\0';
  local_100[4] = '\0';
  local_100[5] = '\0';
  local_100[6] = '\0';
  local_100[7] = '\0';
  local_100[8] = '\0';
  local_100[9] = '\0';
  local_100[10] = '\0';
  local_100[0xb] = '\0';
  local_100[0xc] = '\0';
  local_100[0xd] = '\0';
  local_100[0xe] = '\0';
  local_100[0xf] = '\0';
  iVar4 = FUN_ffff00009078836c(local_b0,0x20);
  if (iVar4 == 0) {
    lVar6 = 0;
    pbVar7 = local_b0;
    do {
      bVar3 = *pbVar7;
      cVar8 = '0';
      cVar2 = '0';
      if (0x9f < bVar3) {
        cVar2 = '7';
      }
      if (9 < (bVar3 & 0xf)) {
        cVar8 = '7';
      }
      lVar1 = lVar6 + 2;
      local_d8[lVar6] = cVar2 + (bVar3 >> 4);
      local_d8[lVar6 + 1] = cVar8 + (bVar3 & 0xf);
      lVar6 = lVar1;
      pbVar7 = pbVar7 + 1;
    } while (lVar1 != 0x20);
    local_d8[0x20] = 0;
    lVar6 = 0;
    pbVar7 = local_b0 + 0x10;
    do {
      bVar3 = *pbVar7;
      cVar8 = '0';
      cVar2 = '0';
      if (0x9f < bVar3) {
        cVar2 = '7';
      }
      if (9 < (bVar3 & 0xf)) {
        cVar8 = '7';
      }
      lVar1 = lVar6 + 2;
      local_100[lVar6] = cVar2 + (bVar3 >> 4);
      local_100[lVar6 + 1] = cVar8 + (bVar3 & 0xf);
      lVar6 = lVar1;
      pbVar7 = pbVar7 + 1;
    } while (lVar1 != 0x20);
    local_100[0x20] = 0;
    FUN_ffff000090709bec(s_dump_socid____ffff0000907fa6e4);
    FUN_ffff000090709bec(local_d8);
    FUN_ffff000090709bec(local_100);
    FUN_ffff000090709bec(s_finish_dump_ffff0000907fe0dd);
    FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  }
  else {
    iVar4 = FUN_ffff00009077f6fc
                      (&local_90,0x80,s_get_socid_failed___Err_0x_x_ffff0000907e383c,iVar4);
    if (iVar4 < 0) {
      pcVar5 = s_unknown_error_ffff0000907db6d8;
    }
    else {
      uStack_18 = uStack_18 & 0xffffffffffffff;
      pcVar5 = (char *)&local_90;
    }
    FUN_ffff000090709c9c(pcVar5);
  }
  return;
}


