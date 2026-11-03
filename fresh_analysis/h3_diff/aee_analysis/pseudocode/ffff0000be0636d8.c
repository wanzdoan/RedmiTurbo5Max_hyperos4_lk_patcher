
ulong FUN_ffff0000be0636d8(ulong param_1,ulong param_2)

{
  ulong uVar1;
  undefined8 uVar2;
  int iVar3;
  long lVar4;
  long lVar5;
  ulong uVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  undefined *puVar11;
  long lVar12;
  ulong local_80;
  long local_68;
  
  uVar9 = DAT_ffff0000be1271b0;
  if (param_1 == 0) {
    uVar8 = 0;
  }
  else {
    lVar4 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
    if (lVar4 == 0) {
      FUN_ffff0000be063b40
                (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
      FUN_ffff0000be05fd98
                (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
      uVar8 = 0xffffffffffffffed;
    }
    else {
      lVar12 = 0x600000;
      if (DAT_ffff0000be1271a8 == '\0') {
        lVar12 = 0x300000;
      }
      uVar8 = *(long *)(lVar4 + 0x20) - lVar12;
      if (*(long *)(lVar4 + 0x20) < lVar12) {
        FUN_ffff0000be063b40(s_kedump__partition_size__llx__is_l_ffff0000be0c6850);
        uVar2 = 0x600000;
        if (DAT_ffff0000be1271a8 == '\0') {
          uVar2 = 0x300000;
        }
        FUN_ffff0000be05fd98
                  (s_kedump__partition_size__llx__is_l_ffff0000be0c6850,
                   *(undefined8 *)(lVar4 + 0x20),uVar2);
        FUN_ffff0000be05def0(lVar4);
        uVar8 = 0xffffffffffffffe4;
      }
      else if ((uVar9 < uVar8) && (param_2 <= uVar8 - uVar9)) {
        local_68 = 0;
        uVar8 = 0;
        puVar11 = PTR_DAT_ffff0000be0f5570;
        uVar7 = param_1;
        local_80 = param_2;
        do {
          iVar3 = FUN_ffff0000be05d9a8
                            (puVar11,s_ke_write_ffff0000be0b047d,0x200000,&local_68,0x15,
                             uVar7 & 0xffffffffffe00000,0,0);
          if (iVar3 != 0) {
            FUN_ffff0000be063b40(s_kedump__map_error_ffff0000be0c28ae);
            FUN_ffff0000be05fd98(s_kedump__map_error_ffff0000be0c28ae);
            FUN_ffff0000be05def0(lVar4);
            uVar8 = (ulong)iVar3;
            goto LAB_ffff0000be063ac8;
          }
          lVar12 = (uVar7 & 0x1fffff) + local_68;
          FUN_ffff0000be063b40(s_kedump__0x_llx_0x_lx_0x_llx_ffff0000be0a9941,param_1,param_2,uVar9)
          ;
          FUN_ffff0000be05fd98(s_kedump__0x_llx_0x_lx_0x_llx_ffff0000be0a9941,param_1,param_2,uVar9)
          ;
          uVar1 = (uVar7 & 0xffffffffffe00000) + 0x200000;
          uVar6 = (ulong)DAT_ffff0000be1271ac;
          uVar7 = uVar1 - uVar7;
          if (local_80 <= uVar7) {
            uVar7 = local_80;
          }
          uVar10 = uVar7;
          if (DAT_ffff0000be1271ac - 1 < 0xfff) {
            if (uVar7 + uVar6 < 0x1000) {
              FUN_ffff0000be0607b0(&DAT_ffff0000be1271b8 + uVar6,lVar12,uVar7);
              DAT_ffff0000be1271ac = DAT_ffff0000be1271ac + (int)uVar7;
            }
            else {
              uVar10 = (ulong)(0x1000 - DAT_ffff0000be1271ac);
              FUN_ffff0000be0607b0(&DAT_ffff0000be1271b8 + uVar6,lVar12,uVar10);
              lVar5 = FUN_ffff0000be05e088
                                (lVar4,&DAT_ffff0000be1271b8,uVar9 - DAT_ffff0000be1271ac,0x1000);
              if (lVar5 < 0) {
                FUN_ffff0000be063b40(s_kedump__handle_last_data_failed___ffff0000be0bd975,lVar5);
                FUN_ffff0000be05fd98(s_kedump__handle_last_data_failed___ffff0000be0bd975,lVar5);
              }
              DAT_ffff0000be1271ac = 0;
            }
            lVar12 = uVar10 + lVar12;
            uVar8 = uVar10 + uVar8;
            uVar9 = uVar10 + uVar9;
            uVar10 = uVar7 - uVar10;
          }
          local_80 = local_80 - uVar7;
          puVar11 = PTR_DAT_ffff0000be0f5570;
          for (; PTR_DAT_ffff0000be0f5570 = puVar11, uVar10 != 0; uVar10 = uVar10 - uVar7) {
            FUN_ffff0000be060ae0(&DAT_ffff0000be1271b8,0,0x1000);
            if (uVar10 < 0x1000) {
              DAT_ffff0000be1271ac = (uint)uVar10;
              FUN_ffff0000be0607b0(&DAT_ffff0000be1271b8,lVar12,uVar10);
              uVar7 = uVar10;
            }
            else {
              FUN_ffff0000be0607b0(&DAT_ffff0000be1271b8,lVar12,0x1000);
              lVar5 = FUN_ffff0000be05e088(lVar4,&DAT_ffff0000be1271b8,uVar9,0x1000);
              uVar7 = 0x1000;
              if (lVar5 < 0) {
                FUN_ffff0000be063b40
                          (s_kedump__write_err__ld___lx__lx___ffff0000be0bc641,lVar5,uVar8,param_2,
                           param_1,uVar9);
                FUN_ffff0000be05fd98
                          (s_kedump__write_err__ld___lx__lx___ffff0000be0bc641,lVar5,uVar8,param_2,
                           param_1,uVar9);
                uVar7 = 0x1000;
              }
            }
            uVar8 = uVar7 + uVar8;
            uVar9 = uVar7 + uVar9;
            lVar12 = uVar7 + lVar12;
            puVar11 = PTR_DAT_ffff0000be0f5570;
          }
          FUN_ffff0000be05dd74(puVar11,local_68);
          uVar7 = uVar1;
        } while (local_80 != 0);
        FUN_ffff0000be05def0(lVar4);
LAB_ffff0000be063ac8:
        if (0 < (long)uVar8) {
          DAT_ffff0000be1271b0 = DAT_ffff0000be1271b0 + uVar8;
        }
      }
      else {
        FUN_ffff0000be063b40
                  (s_kedump__write_oversize__lx__>__l_ffff0000be0c68dd,param_2,uVar9,uVar8);
        FUN_ffff0000be05fd98
                  (s_kedump__write_oversize__lx__>__l_ffff0000be0c68dd,param_2,uVar9,uVar8);
        FUN_ffff0000be05def0(lVar4);
        uVar8 = 0xfffffffffffffff2;
      }
    }
  }
  if (param_2 != (uVar8 & 0xffffffff)) {
    FUN_ffff0000be063b40(s_kedump_W___write_size_not_sync_<_ffff0000be0c68af,uVar8,param_2);
    FUN_ffff0000be05fd98(s_kedump_W___write_size_not_sync_<_ffff0000be0c68af,uVar8,param_2);
  }
  return uVar8;
}


