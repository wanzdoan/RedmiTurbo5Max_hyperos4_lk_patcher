// ffff000090720f10  size=696  FUN_ffff000090720f10

int FUN_ffff000090720f10(byte param_1,long param_2,ulong param_3)

{
  undefined2 *puVar1;
  uint uVar2;
  int iVar3;
  ulong uVar4;
  ulong uVar5;
  char *pcVar6;
  undefined1 *puVar7;
  char cVar8;
  byte bVar9;
  undefined1 *puVar10;
  char cVar11;
  ulong uVar12;
  long lVar13;
  undefined8 local_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined2 local_24;
  
  local_68 = (ulong)param_1;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_24 = CONCAT11(param_1,0x3e);
  iVar3 = FUN_ffff00009072879c(10,0x55,&local_24,2,0);
  if (iVar3 != 0) {
LAB_ffff000090720fa4:
    FUN_ffff00009077de54
              (s__XMCHG_LK_BQ28Z610___s__i2c_writ_ffff0000907e0e1f,
               s_bq28z610_write_byte_ffff0000907e5dad,iVar3);
    FUN_ffff00009077de54
              (s__XMCHG_LK_BQ28Z610___s_I2C_faile_ffff0000907dc411,s_fg_write_block_ffff0000907e43f0
               ,0x3e,param_1);
    return iVar3;
  }
  local_24 = 0x3f;
  iVar3 = FUN_ffff00009072879c(10,0x55,&local_24,2,0);
  if (iVar3 != 0) {
    param_1 = 0;
    goto LAB_ffff000090720fa4;
  }
  FUN_ffff00009077cbd8(80000);
  lVar13 = 0;
  do {
    local_24 = CONCAT11(local_24._1_1_,(char)lVar13 + '>');
    iVar3 = FUN_ffff0000907288e0(10,0x55,&local_24,1,1,0);
    if (iVar3 != 0) {
      FUN_ffff00009077de54
                (s__XMCHG_LK_BQ28Z610___s__i2c_read_ffff0000907e27a3,
                 s_bq28z610_read_byte_ffff0000907d28be,iVar3);
      FUN_ffff00009077de54
                (s__XMCHG_LK_BQ28Z610___s_I2C_faile_ffff0000907e8f89,
                 s_fg_read_block_ffff000090802119,(int)lVar13 + 0x3e);
      return iVar3;
    }
    *(undefined1 *)((long)&local_68 + lVar13) = (undefined1)local_24;
    lVar13 = lVar13 + 1;
  } while (lVar13 != 0x24);
  uVar2 = uStack_48._3_1_ - 2;
  if ((uVar2 & 0xff) == 0) {
    if (uStack_48._2_1_ != 0xff) goto LAB_ffff0000907210c0;
    goto LAB_ffff000090721140;
  }
  uVar4 = (ulong)uVar2 & 0xff;
  if ((uVar2 & 0xff) == 1) {
    uVar5 = 0;
    bVar9 = 0;
LAB_ffff000090721118:
    lVar13 = uVar4 - uVar5;
    pcVar6 = (char *)((long)&local_68 + uVar5);
    do {
      lVar13 = lVar13 + -1;
      bVar9 = *pcVar6 + bVar9;
      pcVar6 = pcVar6 + 1;
    } while (lVar13 != 0);
  }
  else {
    uVar5 = (ulong)uVar2 & 0xfe;
    cVar8 = '\0';
    cVar11 = '\0';
    pcVar6 = (char *)((ulong)&local_68 | 1);
    uVar12 = uVar5;
    do {
      uVar12 = uVar12 - 2;
      cVar8 = pcVar6[-1] + cVar8;
      cVar11 = *pcVar6 + cVar11;
      pcVar6 = pcVar6 + 2;
    } while (uVar12 != 0);
    bVar9 = cVar11 + cVar8;
    if (uVar5 != uVar4) goto LAB_ffff000090721118;
  }
  if (uStack_48._2_1_ != (byte)~bVar9) {
LAB_ffff0000907210c0:
    FUN_ffff00009077de54
              (s__XMCHG_LK_BQ28Z610___s_failed_to_ffff0000907ec0cc,
               s_fg_mac_read_block_ffff0000907d5c6e);
    return 1;
  }
LAB_ffff000090721140:
  if ((param_3 & 0xff) == 0) {
    return 0;
  }
  if (((uint)param_3 & 0xff) == 1) {
    uVar5 = 0;
  }
  else {
    uVar5 = param_3 & 0xfe;
    uVar4 = (ulong)&local_68 | 3;
    lVar13 = param_2 + 1;
    uVar12 = uVar5;
    do {
      puVar1 = (undefined2 *)(uVar4 - 1);
      uVar4 = uVar4 + 2;
      uVar12 = uVar12 - 2;
      *(undefined2 *)(lVar13 + -1) = *puVar1;
      lVar13 = lVar13 + 2;
    } while (uVar12 != 0);
    if (uVar5 == (param_3 & 0xff)) {
      return 0;
    }
  }
  lVar13 = (param_3 & 0xff) - uVar5;
  puVar7 = (undefined1 *)((long)&local_68 + uVar5 + 2);
  puVar10 = (undefined1 *)(param_2 + uVar5);
  do {
    lVar13 = lVar13 + -1;
    *puVar10 = *puVar7;
    puVar7 = puVar7 + 1;
    puVar10 = puVar10 + 1;
  } while (lVar13 != 0);
  return 0;
}


