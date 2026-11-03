// ffff00009076858c  size=1088  FUN_ffff00009076858c

/* WARNING: Type propagation algorithm not settling */

int FUN_ffff00009076858c(uint param_1)

{
  byte bVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  long lVar5;
  long *plVar6;
  undefined8 uVar7;
  long lVar8;
  long lVar9;
  ulong uVar10;
  undefined4 uVar11;
  long lVar12;
  long lVar13;
  long lVar14;
  int local_7c [3];
  long local_70;
  int local_64;
  
  local_70 = 0;
  local_7c[2] = 0;
  local_7c[0] = 1;
  local_7c[1] = 0;
  iVar2 = FUN_ffff0000907c5644();
  if (iVar2 != 0) {
    FUN_ffff00009077de54(s__AVB__Failed_to_alloc_memory_for_ffff000090802c10,iVar2);
    return 0x100000;
  }
  lVar5 = FUN_ffff0000907c5630();
  if (lVar5 == 0) {
    FUN_ffff00009077de54(s__AVB__Bad_AVB_Operation_Pointer_ffff0000907dd1d1);
    return 0x100006;
  }
  if (param_1 == 0) {
    thunk_FUN_ffff000090798838(&DAT_ffff0000907d0a5b);
  }
  else if (param_1 == 1) {
    thunk_FUN_ffff000090798838(s_recovery_ffff0000907e5a90);
  }
  else {
    FUN_ffff00009077de54(s__AVB__invalid_boot_image_type_ffff0000907e37f1);
  }
  local_64 = 4;
  FUN_ffff000090788750(&local_64);
  bVar1 = local_64 == 3;
  uVar11 = 2;
  if (!(bool)bVar1) {
    uVar11 = 3;
  }
  FUN_ffff00009077de54(s__AVB__img_auth_required____d_ffff0000907f5283,!(bool)bVar1);
  FUN_ffff0000907692f4(uVar11);
  if (param_1 == 1) {
    plVar6 = (long *)FUN_ffff00009077d24c(0x18);
    if (plVar6 == (long *)0x0) goto LAB_ffff0000907687dc;
    FUN_ffff00009077e858(plVar6,&PTR_s_recovery_ffff000090821960,0x18);
    bVar1 = bVar1 | 4;
  }
  else {
    if ((param_1 != 0) || (plVar6 = (long *)FUN_ffff00009077d24c(0x28), plVar6 == (long *)0x0)) {
LAB_ffff0000907687dc:
      iVar2 = 1;
      goto LAB_ffff0000907688dc;
    }
    FUN_ffff00009077e858(plVar6,&PTR_DAT_ffff000090821938,0x28);
    uVar7 = FUN_ffff00009071f608();
    lVar14 = 0;
    do {
      lVar13 = *(long *)((long)&PTR_s_vendor_boot_ffff000090821978 + lVar14);
      iVar3 = FUN_ffff00009077ef88(lVar13);
      iVar4 = FUN_ffff00009077ef88(uVar7);
      iVar2 = iVar4 + iVar3 + 1;
      lVar8 = FUN_ffff00009077d24c(iVar2);
      if (lVar8 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c3a,0x72,&DAT_ffff0000907d24c9);
      }
      FUN_ffff00009077eb88(lVar8,0,iVar2);
      iVar2 = FUN_ffff00009077f434(lVar8,iVar2,&DAT_ffff0000907e097c,lVar13,uVar7);
      if (0 < iVar2) {
        *(undefined1 *)(lVar8 + (ulong)(uint)(iVar4 + iVar3)) = 0;
      }
      lVar9 = FUN_ffff00009077bd60(lVar8);
      if (lVar9 == 0) {
        FUN_ffff00009077de54(s_partition__s_not_exists_ffff0000907d9c65,lVar13);
        thunk_FUN_ffff0000907c2ad8(lVar8);
        break;
      }
      lVar12 = 0;
      if (*plVar6 != 0) {
        if (plVar6[1] == 0) {
          lVar12 = 1;
        }
        else if (plVar6[2] == 0) {
          lVar12 = 2;
        }
        else if (plVar6[3] == 0) {
          lVar12 = 3;
        }
        else {
          if (plVar6[4] != 0) {
            FUN_ffff00009077de54(s_Not_enough_space_to_add__s_to_re_ffff0000907f1bc9,lVar13);
                    /* WARNING: Subroutine does not return */
            FUN_ffff00009077cd04
                      (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c3a,0x8b,&DAT_ffff0000907d24c9
                      );
          }
          lVar12 = 4;
        }
      }
      plVar6[lVar12] = lVar13;
      FUN_ffff00009077de54(s__s_exists_ffff0000907d6ac6,lVar13);
      thunk_FUN_ffff0000907c2ad8(lVar8);
      FUN_ffff00009077bce4(lVar9);
      lVar14 = lVar14 + 8;
    } while (lVar14 != 0x18);
  }
  FUN_ffff000090768270(local_7c + 1);
  uVar10 = FUN_ffff000090766998();
  if ((uVar10 & 1) == 0) {
    bVar1 = bVar1 | 2;
  }
  FUN_ffff00009076825c(local_7c + 2);
  iVar2 = FUN_ffff0000907c5f5c(lVar5,s_seccfg_ffff0000907d1c33,0x200);
  if (iVar2 == 0) {
    if (param_1 < 2) {
      uVar7 = FUN_ffff00009071f608();
      iVar2 = FUN_ffff0000907be0cc(lVar5,plVar6,uVar7,bVar1,local_7c[2],&local_70);
      if (((bVar1 & 1) != 0) || (iVar2 == 0)) {
        iVar2 = FUN_ffff000090768a54(lVar5,param_1,local_70);
        if (iVar2 == 0) {
          if ((bVar1 & 1) == 0) {
            FUN_ffff0000907692f4(0);
          }
          iVar2 = FUN_ffff0000907689cc(param_1);
          if (iVar2 != 0) {
            FUN_ffff000090768fd0(local_70);
          }
        }
        goto LAB_ffff00009076884c;
      }
    }
    else {
      iVar2 = 8;
    }
  }
  else {
    FUN_ffff00009077de54(s_init_persist_value_ret___0x_x_ffff0000907d004c,iVar2);
LAB_ffff00009076884c:
    iVar2 = 0;
  }
  thunk_FUN_ffff0000907c2ad8(plVar6);
  if (local_70 != 0) {
    local_7c[2] = *(undefined4 *)(local_70 + 0x130);
  }
LAB_ffff0000907688dc:
  FUN_ffff00009077de54(s__AVB__avb_ret____d_ffff0000907de9e3,iVar2);
  iVar2 = FUN_ffff000090788750(local_7c);
  if ((iVar2 == 0) && (local_7c[0] == 3)) {
    FUN_ffff0000907692f4(2);
  }
  FUN_ffff0000907692a0();
  if (param_1 == 0) {
    FUN_ffff000090769308(&DAT_ffff0000907d0a5b);
  }
  else if (param_1 == 1) {
    FUN_ffff000090769308(s_recovery_ffff0000907e5a90);
  }
  else {
    FUN_ffff00009077de54(s__AVB__invalid_boot_image_type_ffff0000907e37f1);
  }
  FUN_ffff0000907692ac();
  FUN_ffff0000907692e0();
  FUN_ffff0000907c4868();
  FUN_ffff0000907c4670();
  FUN_ffff000090768188(local_7c[2]);
  return iVar2;
}


