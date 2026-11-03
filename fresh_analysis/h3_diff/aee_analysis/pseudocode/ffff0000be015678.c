
void FUN_ffff0000be015678(void)

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
  local_70 = (char  [8])s_partition_type__ffff0000be0ac5f2._0_8_;
  acStack_68 = (char  [8])s_partition_type__ffff0000be0ac5f2._8_8_;
  local_80 = (char  [8])s_partition_size__ffff0000be0af5f4._0_8_;
  acStack_78 = (char  [8])s_partition_size__ffff0000be0af5f4._8_8_;
  local_90 = (char  [8])s_has_slot__ffff0000be0c4f1f._0_8_;
  local_88 = 0x3a;
  plVar3 = (long *)FUN_ffff0000be015664();
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
        lVar4 = FUN_ffff0000be060e5c(lVar10);
        if (lVar4 != 0) break;
        lVar10 = *plVar7;
        plVar3 = plVar7;
        if (lVar10 == 0) goto LAB_ffff0000be0159f0;
      }
      FUN_ffff0000be0611dc(&local_d8,0x40,&DAT_ffff0000be0b6205,lVar10);
      iVar1 = FUN_ffff0000be060e5c(&local_d8);
      iVar2 = FUN_ffff0000be060e5c(local_90);
      lVar4 = (long)(iVar2 + iVar1 + 1);
      lVar10 = FUN_ffff0000be05f1bc(lVar4);
      if (lVar10 == 0) goto LAB_ffff0000be0159e4;
      FUN_ffff0000be0611dc(lVar10,lVar4,&DAT_ffff0000be0af57f,local_90,*plVar3);
      lVar4 = FUN_ffff0000be05df6c(&local_d8);
      puVar6 = &DAT_ffff0000be0ae542;
      lVar8 = lVar4;
      if (lVar4 == 0) {
        FUN_ffff0000be0611dc(&local_d8,0x40,&DAT_ffff0000be0ac42b,*plVar3);
        lVar8 = FUN_ffff0000be05df6c(&local_d8);
        if (lVar8 != 0) {
          puVar6 = &DAT_ffff0000be0bac01;
          goto LAB_ffff0000be0157d8;
        }
        goto LAB_ffff0000be0159c4;
      }
LAB_ffff0000be0157d8:
      FUN_ffff0000be013510(lVar10,puVar6);
      thunk_FUN_ffff0000be09937c(lVar10);
      iVar1 = FUN_ffff0000be060e5c(&local_d8);
      iVar2 = FUN_ffff0000be060e5c(local_80);
      iVar1 = iVar2 + iVar1 + 1;
      lVar9 = (long)iVar1;
      lVar10 = FUN_ffff0000be05f1bc(lVar9);
      if (lVar10 == 0) {
LAB_ffff0000be0159e4:
        FUN_ffff0000be05fd98(s_Publish_partition_error__ffff0000be0ae54c);
        break;
      }
      FUN_ffff0000be0611dc(lVar10,lVar9,&DAT_ffff0000be0af57f,local_80,&local_d8);
      lVar5 = FUN_ffff0000be05f1bc(lVar9);
      if (lVar5 == 0) {
LAB_ffff0000be0159dc:
        thunk_FUN_ffff0000be09937c(lVar10);
        goto LAB_ffff0000be0159e4;
      }
      iVar2 = FUN_ffff0000be0611dc(lVar5,lVar9,&DAT_ffff0000be0be072,*(undefined8 *)(lVar8 + 0x20));
      if ((-1 < iVar2) && (iVar2 < iVar1)) {
        FUN_ffff0000be013510(lVar10,lVar5);
      }
      thunk_FUN_ffff0000be09937c(lVar10);
      if (lVar4 != 0) {
        iVar1 = FUN_ffff0000be060e5c(&local_d8);
        iVar2 = FUN_ffff0000be060e5c(local_80);
        iVar1 = iVar2 + iVar1 + 1;
        lVar9 = FUN_ffff0000be05f1bc((long)iVar1);
        lVar10 = lVar5;
        if (lVar9 == 0) goto LAB_ffff0000be0159dc;
        iVar2 = FUN_ffff0000be0611dc(lVar9,(long)iVar1,s__s_s_b_ffff0000be0a8e42,local_80,*plVar3);
        if ((-1 < iVar2) && (iVar2 < iVar1)) {
          FUN_ffff0000be013510(lVar9,lVar5);
        }
        thunk_FUN_ffff0000be09937c(lVar9);
      }
      thunk_FUN_ffff0000be09937c(lVar5);
      iVar1 = FUN_ffff0000be060e5c(&local_d8);
      iVar2 = FUN_ffff0000be060e5c(local_70);
      lVar9 = (long)(iVar2 + iVar1 + 1);
      lVar10 = FUN_ffff0000be05f1bc(lVar9);
      if (lVar10 == 0) goto LAB_ffff0000be0159e4;
      FUN_ffff0000be0611dc(lVar10,lVar9,&DAT_ffff0000be0af57f,local_70,&local_d8);
      FUN_ffff0000be027df8(&local_d8,&local_94);
      if (local_94 < 3) {
        FUN_ffff0000be013510(lVar10,(long)local_94 * 5 + -0xffff41f29ef8);
        thunk_FUN_ffff0000be09937c(lVar10);
        if (lVar4 != 0) {
          iVar1 = FUN_ffff0000be060e5c(&local_d8);
          iVar2 = FUN_ffff0000be060e5c(local_70);
          iVar1 = iVar2 + iVar1 + 1;
          lVar10 = FUN_ffff0000be05f1bc((long)iVar1);
          if (lVar10 == 0) goto LAB_ffff0000be0159e4;
          iVar2 = FUN_ffff0000be0611dc
                            (lVar10,(long)iVar1,s__s_s_b_ffff0000be0a8e42,local_70,*plVar3);
          if ((-1 < iVar2) && (iVar2 < iVar1)) {
            FUN_ffff0000be013510(lVar10,(long)local_94 * 5 + -0xffff41f29ef8);
          }
          goto LAB_ffff0000be0159c4;
        }
      }
      else {
LAB_ffff0000be0159c4:
        thunk_FUN_ffff0000be09937c(lVar10);
      }
      lVar10 = *plVar7;
      plVar3 = plVar7;
    } while (lVar10 != 0);
LAB_ffff0000be0159f0:
    if (lVar8 != 0) {
      FUN_ffff0000be05def0(lVar8);
    }
  }
  return;
}


