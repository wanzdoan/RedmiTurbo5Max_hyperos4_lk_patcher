// ffff000090768854  size=1088  FUN_ffff000090768854

/* WARNING: Type propagation algorithm not settling */

int FUN_ffff000090768854(uint param_1)

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
  iVar2 = FUN_ffff0000907c5cb8();
  if (iVar2 != 0) {
    FUN_ffff00009077e11c(s__AVB__Failed_to_alloc_memory_for_ffff000090802e14,iVar2);
    return 0x100000;
  }
  lVar5 = FUN_ffff0000907c5ca4();
  if (lVar5 == 0) {
    FUN_ffff00009077e11c(s__AVB__Bad_AVB_Operation_Pointer_ffff0000907dd2a7);
    return 0x100006;
  }
  if (param_1 == 0) {
    thunk_FUN_ffff000090798af8(&DAT_ffff0000907d0a72);
  }
  else if (param_1 == 1) {
    thunk_FUN_ffff000090798af8(s_recovery_ffff0000907e5ba4);
  }
  else {
    FUN_ffff00009077e11c(s__AVB__invalid_boot_image_type_ffff0000907e38c2);
  }
  local_64 = 4;
  FUN_ffff000090788a10(&local_64);
  bVar1 = local_64 == 3;
  uVar11 = 2;
  if (!(bool)bVar1) {
    uVar11 = 3;
  }
  FUN_ffff00009077e11c(s__AVB__img_auth_required____d_ffff0000907f53f0,!(bool)bVar1);
  FUN_ffff0000907695bc(uVar11);
  if (param_1 == 1) {
    plVar6 = (long *)FUN_ffff00009077d514(0x18);
    if (plVar6 == (long *)0x0) goto LAB_ffff000090768aa4;
    FUN_ffff00009077eb20(plVar6,&PTR_s_recovery_ffff000090821980,0x18);
    bVar1 = bVar1 | 4;
  }
  else {
    if ((param_1 != 0) || (plVar6 = (long *)FUN_ffff00009077d514(0x28), plVar6 == (long *)0x0)) {
LAB_ffff000090768aa4:
      iVar2 = 1;
      goto LAB_ffff000090768ba4;
    }
    FUN_ffff00009077eb20(plVar6,&PTR_DAT_ffff000090821958,0x28);
    uVar7 = FUN_ffff00009071f790();
    lVar14 = 0;
    do {
      lVar13 = *(long *)((long)&PTR_s_vendor_boot_ffff000090821998 + lVar14);
      iVar3 = FUN_ffff00009077f250(lVar13);
      iVar4 = FUN_ffff00009077f250(uVar7);
      iVar2 = iVar4 + iVar3 + 1;
      lVar8 = FUN_ffff00009077d514(iVar2);
      if (lVar8 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c77,0x72,&DAT_ffff0000907d2506);
      }
      FUN_ffff00009077ee50(lVar8,0,iVar2);
      iVar2 = FUN_ffff00009077f6fc(lVar8,iVar2,&DAT_ffff0000907e0a1f,lVar13,uVar7);
      if (0 < iVar2) {
        *(undefined1 *)(lVar8 + (ulong)(uint)(iVar4 + iVar3)) = 0;
      }
      lVar9 = FUN_ffff00009077c028(lVar8);
      if (lVar9 == 0) {
        FUN_ffff00009077e11c(s_partition__s_not_exists_ffff0000907d9cfc,lVar13);
        thunk_FUN_ffff0000907c314c(lVar8);
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
            FUN_ffff00009077e11c(s_Not_enough_space_to_add__s_to_re_ffff0000907f1d55,lVar13);
                    /* WARNING: Subroutine does not return */
            FUN_ffff00009077cfcc
                      (s_app_mt_boot_avb_load_vfy_boot_c_ffff0000907d1c77,0x8b,&DAT_ffff0000907d2506
                      );
          }
          lVar12 = 4;
        }
      }
      plVar6[lVar12] = lVar13;
      FUN_ffff00009077e11c(s__s_exists_ffff0000907d6b5c,lVar13);
      thunk_FUN_ffff0000907c314c(lVar8);
      FUN_ffff00009077bfac(lVar9);
      lVar14 = lVar14 + 8;
    } while (lVar14 != 0x18);
  }
  FUN_ffff000090768508(local_7c + 1);
  uVar10 = FUN_ffff000090766c30();
  if ((uVar10 & 1) == 0) {
    bVar1 = bVar1 | 2;
  }
  FUN_ffff0000907684f4(local_7c + 2);
  iVar2 = FUN_ffff0000907c65d0(lVar5,s_seccfg_ffff0000907d1c70,0x200);
  if (iVar2 == 0) {
    if (param_1 < 2) {
      uVar7 = FUN_ffff00009071f790();
      iVar2 = FUN_ffff0000907be740(lVar5,plVar6,uVar7,bVar1,local_7c[2],&local_70);
      if (((bVar1 & 1) != 0) || (iVar2 == 0)) {
        iVar2 = FUN_ffff000090768d1c(lVar5,param_1,local_70);
        if (iVar2 == 0) {
          if ((bVar1 & 1) == 0) {
            FUN_ffff0000907695bc(0);
          }
          iVar2 = FUN_ffff000090768c94(param_1);
          if (iVar2 != 0) {
            FUN_ffff000090769298(local_70);
          }
        }
        goto LAB_ffff000090768b14;
      }
    }
    else {
      iVar2 = 8;
    }
  }
  else {
    FUN_ffff00009077e11c(s_init_persist_value_ret___0x_x_ffff0000907d006b,iVar2);
LAB_ffff000090768b14:
    iVar2 = 0;
  }
  thunk_FUN_ffff0000907c314c(plVar6);
  if (local_70 != 0) {
    local_7c[2] = *(undefined4 *)(local_70 + 0x130);
  }
LAB_ffff000090768ba4:
  FUN_ffff00009077e11c(s__AVB__avb_ret____d_ffff0000907dea86,iVar2);
  iVar2 = FUN_ffff000090788a10(local_7c);
  if ((iVar2 == 0) && (local_7c[0] == 3)) {
    FUN_ffff0000907695bc(2);
  }
  FUN_ffff000090769568();
  if (param_1 == 0) {
    FUN_ffff0000907695d0(&DAT_ffff0000907d0a72);
  }
  else if (param_1 == 1) {
    FUN_ffff0000907695d0(s_recovery_ffff0000907e5ba4);
  }
  else {
    FUN_ffff00009077e11c(s__AVB__invalid_boot_image_type_ffff0000907e38c2);
  }
  FUN_ffff000090769574();
  FUN_ffff0000907695a8();
  FUN_ffff0000907c4edc();
  FUN_ffff0000907c4ce4();
  FUN_ffff000090768420(local_7c[2]);
  return iVar2;
}


