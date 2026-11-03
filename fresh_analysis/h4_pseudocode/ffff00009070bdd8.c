
void FUN_ffff00009070bdd8(void)

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
  local_70 = (char  [8])s_partition_type__ffff0000907dbf6c._0_8_;
  acStack_68 = (char  [8])s_partition_type__ffff0000907dbf6c._8_8_;
  local_80 = (char  [8])s_partition_size__ffff0000907e0ad1._0_8_;
  acStack_78 = (char  [8])s_partition_size__ffff0000907e0ad1._8_8_;
  local_90 = (char  [8])s_has_slot__ffff0000908003cc._0_8_;
  local_88 = 0x3a;
  plVar3 = (long *)FUN_ffff00009070bdc4();
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
        lVar4 = FUN_ffff00009077f250(lVar10);
        if (lVar4 != 0) break;
        lVar10 = *plVar7;
        plVar3 = plVar7;
        if (lVar10 == 0) goto LAB_ffff00009070c150;
      }
      FUN_ffff00009077f6fc(&local_d8,0x40,&DAT_ffff0000907ea6bc,lVar10);
      iVar1 = FUN_ffff00009077f250(&local_d8);
      iVar2 = FUN_ffff00009077f250(local_90);
      lVar4 = (long)(iVar2 + iVar1 + 1);
      lVar10 = FUN_ffff00009077d514(lVar4);
      if (lVar10 == 0) goto LAB_ffff00009070c144;
      FUN_ffff00009077f6fc(lVar10,lVar4,&DAT_ffff0000907e0a1f,local_90,*plVar3);
      lVar4 = FUN_ffff00009077c028(&local_d8);
      puVar6 = &DAT_ffff0000907df35d;
      lVar8 = lVar4;
      if (lVar4 == 0) {
        FUN_ffff00009077f6fc(&local_d8,0x40,&DAT_ffff0000907dbf7c,*plVar3);
        lVar8 = FUN_ffff00009077c028(&local_d8);
        if (lVar8 != 0) {
          puVar6 = &DAT_ffff0000907f0cf7;
          goto LAB_ffff00009070bf38;
        }
        goto LAB_ffff00009070c124;
      }
LAB_ffff00009070bf38:
      FUN_ffff0000907099c4(lVar10,puVar6);
      thunk_FUN_ffff0000907c314c(lVar10);
      iVar1 = FUN_ffff00009077f250(&local_d8);
      iVar2 = FUN_ffff00009077f250(local_80);
      iVar1 = iVar2 + iVar1 + 1;
      lVar9 = (long)iVar1;
      lVar10 = FUN_ffff00009077d514(lVar9);
      if (lVar10 == 0) {
LAB_ffff00009070c144:
        FUN_ffff00009077e11c(s_Publish_partition_error__ffff0000907df367);
        break;
      }
      FUN_ffff00009077f6fc(lVar10,lVar9,&DAT_ffff0000907e0a1f,local_80,&local_d8);
      lVar5 = FUN_ffff00009077d514(lVar9);
      if (lVar5 == 0) {
LAB_ffff00009070c13c:
        thunk_FUN_ffff0000907c314c(lVar10);
        goto LAB_ffff00009070c144;
      }
      iVar2 = FUN_ffff00009077f6fc(lVar5,lVar9,&DAT_ffff0000907f5c75,*(undefined8 *)(lVar8 + 0x20));
      if ((-1 < iVar2) && (iVar2 < iVar1)) {
        FUN_ffff0000907099c4(lVar10,lVar5);
      }
      thunk_FUN_ffff0000907c314c(lVar10);
      if (lVar4 != 0) {
        iVar1 = FUN_ffff00009077f250(&local_d8);
        iVar2 = FUN_ffff00009077f250(local_80);
        iVar1 = iVar2 + iVar1 + 1;
        lVar9 = FUN_ffff00009077d514((long)iVar1);
        lVar10 = lVar5;
        if (lVar9 == 0) goto LAB_ffff00009070c13c;
        iVar2 = FUN_ffff00009077f6fc(lVar9,(long)iVar1,s__s_s_b_ffff0000907d73b5,local_80,*plVar3);
        if ((-1 < iVar2) && (iVar2 < iVar1)) {
          FUN_ffff0000907099c4(lVar9,lVar5);
        }
        thunk_FUN_ffff0000907c314c(lVar9);
      }
      thunk_FUN_ffff0000907c314c(lVar5);
      iVar1 = FUN_ffff00009077f250(&local_d8);
      iVar2 = FUN_ffff00009077f250(local_70);
      lVar9 = (long)(iVar2 + iVar1 + 1);
      lVar10 = FUN_ffff00009077d514(lVar9);
      if (lVar10 == 0) goto LAB_ffff00009070c144;
      FUN_ffff00009077f6fc(lVar10,lVar9,&DAT_ffff0000907e0a1f,local_70,&local_d8);
      FUN_ffff000090734fbc(&local_d8,&local_94);
      if (local_94 < 3) {
        FUN_ffff0000907099c4(lVar10,(long)local_94 * 5 + -0xffff6f7e92b0);
        thunk_FUN_ffff0000907c314c(lVar10);
        if (lVar4 != 0) {
          iVar1 = FUN_ffff00009077f250(&local_d8);
          iVar2 = FUN_ffff00009077f250(local_70);
          iVar1 = iVar2 + iVar1 + 1;
          lVar10 = FUN_ffff00009077d514((long)iVar1);
          if (lVar10 == 0) goto LAB_ffff00009070c144;
          iVar2 = FUN_ffff00009077f6fc
                            (lVar10,(long)iVar1,s__s_s_b_ffff0000907d73b5,local_70,*plVar3);
          if ((-1 < iVar2) && (iVar2 < iVar1)) {
            FUN_ffff0000907099c4(lVar10,(long)local_94 * 5 + -0xffff6f7e92b0);
          }
          goto LAB_ffff00009070c124;
        }
      }
      else {
LAB_ffff00009070c124:
        thunk_FUN_ffff0000907c314c(lVar10);
      }
      lVar10 = *plVar7;
      plVar3 = plVar7;
    } while (lVar10 != 0);
LAB_ffff00009070c150:
    if (lVar8 != 0) {
      FUN_ffff00009077bfac(lVar8);
    }
  }
  return;
}


