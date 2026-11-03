// ffff00009072b394  size=600  FUN_ffff00009072b394

undefined8 FUN_ffff00009072b394(void)

{
  ulong uVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  long lVar5;
  ulong uVar6;
  ulong uVar7;
  undefined4 local_94;
  uint local_90;
  undefined4 local_8c;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined4 local_58;
  
  lVar3 = FUN_ffff00009071935c();
  if (lVar3 != 0) {
    FUN_ffff000090707c70(lVar3,0x100);
    lVar4 = FUN_ffff000090719370();
    if (lVar4 != 0) {
      FUN_ffff000090707c70(lVar4,*(undefined4 *)(lVar3 + 0x1c));
    }
  }
  lVar4 = FUN_ffff00009071935c();
  lVar3 = FUN_ffff000090719370();
  if ((lVar4 == 0) || (lVar3 == 0)) {
    FUN_ffff00009077e11c
              (s__s__sram_log_header_is_null__0x__ffff0000907e916c,s_LOG_STORE_LK_ffff0000907d75aa,
               lVar4,lVar3);
  }
  else {
    local_58 = 0;
    local_68 = 0;
    uStack_60 = 0;
    local_78 = 0;
    uStack_70 = 0;
    local_88 = 0;
    uStack_80 = 0;
    FUN_ffff00009077ee50(&local_88,0,0x34);
    iVar2 = FUN_ffff00009072ae44(&local_88);
    if (iVar2 < 0) {
      FUN_ffff00009077e11c
                (s__s__read_emmc_config_not_correct_ffff0000907e10b2,s_LOG_STORE_LK_ffff0000907d75aa
                );
    }
    else {
      uVar6 = (ulong)(uint)(*(int *)(lVar4 + 0x4c) + *(int *)(lVar4 + 0x48) + *(int *)(lVar4 + 0x58)
                           );
      uVar1 = 0x100000;
      if (DAT_ffff00009086d6d0 == '\0') {
        uVar1 = 0x80000;
      }
      lVar4 = 0;
      if (uVar1 <= uVar6) {
        lVar4 = uVar6 - uVar1;
      }
      if (uVar6 <= uVar1) {
        uVar1 = uVar6;
      }
      if ((uVar1 & 3) != 0) {
        uVar1 = (ulong)((int)uVar1 + 4) & 0x3ffffc;
      }
      lVar5 = FUN_ffff00009077c028(s_expdb_ffff0000907d5780);
      if (lVar5 == 0) {
        FUN_ffff00009077e11c
                  (s__s__Partition___s__is_not_exist__ffff0000907fd165,
                   s_LOG_STORE_LK_ffff0000907d75aa,s_expdb_ffff0000907d5780);
      }
      else {
        lVar3 = lVar3 + lVar4;
        lVar4 = 0x400000;
        if (DAT_ffff00009086d6d0 == '\0') {
          lVar4 = 0x200000;
        }
        uVar6 = lVar4 - (*(long *)(lVar5 + 0x28) + (ulong)local_88._4_4_);
        lVar4 = -0x400000;
        if (DAT_ffff00009086d6d0 == '\0') {
          lVar4 = -0x200000;
        }
        uVar7 = uVar1 - uVar6;
        lVar4 = lVar4 + (ulong)local_88._4_4_ + *(long *)(lVar5 + 0x20);
        local_90 = local_88._4_4_;
        if (uVar1 < uVar6 || uVar7 == 0) {
          FUN_ffff00009077c144(lVar5,lVar3,lVar4,uVar1);
          uVar7 = (ulong)(local_88._4_4_ + (int)uVar1);
        }
        else {
          FUN_ffff00009077c144(lVar5,lVar3,lVar4,uVar6);
          lVar4 = -0x400000;
          if (DAT_ffff00009086d6d0 == '\0') {
            lVar4 = -0x200000;
          }
          FUN_ffff00009077c144(lVar5,lVar3 + uVar6,lVar4 + *(long *)(lVar5 + 0x20),uVar7);
        }
        local_8c = (undefined4)uVar7;
        local_88 = CONCAT44(local_8c,(undefined4)local_88);
        local_94 = 1;
        FUN_ffff00009077c144
                  (lVar5,&local_94,
                   (*(long *)(lVar5 + 0x20) - *(long *)(lVar5 + 0x28)) + (uStack_80 >> 0x20) * 0xc +
                   0x34,0xc);
        uStack_80 = CONCAT44(uStack_80._4_4_ + 1,(undefined4)uStack_80) & 0x1fffffffff;
        FUN_ffff00009077c144(lVar5,&local_88,*(long *)(lVar5 + 0x20) - *(long *)(lVar5 + 0x28),0x34)
        ;
        FUN_ffff00009077bfac(lVar5);
      }
    }
  }
  return 0;
}


