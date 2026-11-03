// ffff00009070bd60  size=936  FUN_ffff00009070bd60

void FUN_ffff00009070bd60(void)

{
  int iVar1;
  int iVar2;
  long *plVar3;
  long lVar4;
  long lVar5;
  undefined *puVar6;
  long *plVar7;
  long lVar8;
  long lVar9;
  long lVar10;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  int local_94;
  char local_90 [8];
  undefined2 local_88;
  char local_80 [8];
  char acStack_78 [8];
  char local_70 [8];
  char acStack_68 [8];
  
  local_94 = 0;
  local_70 = (char  [8])s_partition_type__ffff0000907dbe4a._0_8_;
  acStack_68 = (char  [8])s_partition_type__ffff0000907dbe4a._8_8_;
  local_80 = (char  [8])s_partition_size__ffff0000907e0a17._0_8_;
  acStack_78 = (char  [8])s_partition_size__ffff0000907e0a17._8_8_;
  local_90 = (char  [8])s_has_slot__ffff000090800224._0_8_;
  local_88 = 0x3a;
  plVar3 = (long *)FUN_ffff00009070bd4c();
  local_a8 = 0;
  uStack_a0 = 0;
  local_b8 = 0;
  uStack_b0 = 0;
  local_c8 = 0;
  uStack_c0 = 0;
  local_d8 = 0;
  uStack_d0 = 0;
  lVar10 = *plVar3;
  if (lVar10 != 0) {
    lVar8 = 0;
    do {
      while( true ) {
        plVar7 = plVar3 + 1;
        lVar4 = FUN_ffff00009077ef88(lVar10);
        if (lVar4 != 0) break;
        lVar10 = *plVar7;
        plVar3 = plVar7;
        if (lVar10 == 0) goto LAB_ffff00009070c0d8;
      }
      FUN_ffff00009077f434(&local_d8,0x40,&DAT_ffff0000907ea54b,lVar10);
      iVar1 = FUN_ffff00009077ef88(&local_d8);
      iVar2 = FUN_ffff00009077ef88(local_90);
      lVar4 = (long)(iVar2 + iVar1 + 1);
      lVar10 = FUN_ffff00009077d24c(lVar4);
      if (lVar10 == 0) goto LAB_ffff00009070c0cc;
      FUN_ffff00009077f434(lVar10,lVar4,&DAT_ffff0000907e0965,local_90,*plVar3);
      lVar4 = FUN_ffff00009077bd60(&local_d8);
      puVar6 = &DAT_ffff0000907df2a3;
      lVar8 = lVar4;
      if (lVar4 == 0) {
        FUN_ffff00009077f434(&local_d8,0x40,&DAT_ffff0000907dbe5a,*plVar3);
        lVar8 = FUN_ffff00009077bd60(&local_d8);
        if (lVar8 != 0) {
          puVar6 = &DAT_ffff0000907f0b6f;
          goto LAB_ffff00009070bec0;
        }
        goto LAB_ffff00009070c0ac;
      }
LAB_ffff00009070bec0:
      FUN_ffff000090709a38(lVar10,puVar6);
      thunk_FUN_ffff0000907c2ad8(lVar10);
      iVar1 = FUN_ffff00009077ef88(&local_d8);
      iVar2 = FUN_ffff00009077ef88(local_80);
      iVar1 = iVar2 + iVar1 + 1;
      lVar9 = (long)iVar1;
      lVar10 = FUN_ffff00009077d24c(lVar9);
      if (lVar10 == 0) {
LAB_ffff00009070c0cc:
        FUN_ffff00009077de54(s_Publish_partition_error__ffff0000907df2ad);
        break;
      }
      FUN_ffff00009077f434(lVar10,lVar9,&DAT_ffff0000907e0965,local_80,&local_d8);
      lVar5 = FUN_ffff00009077d24c(lVar9);
      if (lVar5 == 0) {
LAB_ffff00009070c0c4:
        thunk_FUN_ffff0000907c2ad8(lVar10);
        goto LAB_ffff00009070c0cc;
      }
      iVar2 = FUN_ffff00009077f434(lVar5,lVar9,&DAT_ffff0000907f5ae7,*(undefined8 *)(lVar8 + 0x20));
      if ((-1 < iVar2) && (iVar2 < iVar1)) {
        FUN_ffff000090709a38(lVar10,lVar5);
      }
      thunk_FUN_ffff0000907c2ad8(lVar10);
      if (lVar4 != 0) {
        iVar1 = FUN_ffff00009077ef88(&local_d8);
        iVar2 = FUN_ffff00009077ef88(local_80);
        iVar1 = iVar2 + iVar1 + 1;
        lVar9 = FUN_ffff00009077d24c((long)iVar1);
        lVar10 = lVar5;
        if (lVar9 == 0) goto LAB_ffff00009070c0c4;
        iVar2 = FUN_ffff00009077f434(lVar9,(long)iVar1,s__s_s_b_ffff0000907d731f,local_80,*plVar3);
        if ((-1 < iVar2) && (iVar2 < iVar1)) {
          FUN_ffff000090709a38(lVar9,lVar5);
        }
        thunk_FUN_ffff0000907c2ad8(lVar9);
      }
      thunk_FUN_ffff0000907c2ad8(lVar5);
      iVar1 = FUN_ffff00009077ef88(&local_d8);
      iVar2 = FUN_ffff00009077ef88(local_70);
      lVar9 = (long)(iVar2 + iVar1 + 1);
      lVar10 = FUN_ffff00009077d24c(lVar9);
      if (lVar10 == 0) goto LAB_ffff00009070c0cc;
      FUN_ffff00009077f434(lVar10,lVar9,&DAT_ffff0000907e0965,local_70,&local_d8);
      FUN_ffff000090734d24(&local_d8,&local_94);
      if (local_94 < 3) {
        FUN_ffff000090709a38(lVar10,(long)local_94 * 5 + -0xffff6f7e92c0);
        thunk_FUN_ffff0000907c2ad8(lVar10);
        if (lVar4 != 0) {
          iVar1 = FUN_ffff00009077ef88(&local_d8);
          iVar2 = FUN_ffff00009077ef88(local_70);
          iVar1 = iVar2 + iVar1 + 1;
          lVar10 = FUN_ffff00009077d24c((long)iVar1);
          if (lVar10 == 0) goto LAB_ffff00009070c0cc;
          iVar2 = FUN_ffff00009077f434
                            (lVar10,(long)iVar1,s__s_s_b_ffff0000907d731f,local_70,*plVar3);
          if ((-1 < iVar2) && (iVar2 < iVar1)) {
            FUN_ffff000090709a38(lVar10,(long)local_94 * 5 + -0xffff6f7e92c0);
          }
          goto LAB_ffff00009070c0ac;
        }
      }
      else {
LAB_ffff00009070c0ac:
        thunk_FUN_ffff0000907c2ad8(lVar10);
      }
      lVar10 = *plVar7;
      plVar3 = plVar7;
    } while (lVar10 != 0);
LAB_ffff00009070c0d8:
    if (lVar8 != 0) {
      FUN_ffff00009077bce4(lVar8);
    }
  }
  return;
}


