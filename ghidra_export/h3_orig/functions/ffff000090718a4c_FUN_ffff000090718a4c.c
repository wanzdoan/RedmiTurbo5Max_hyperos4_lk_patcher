// ffff000090718a4c  size=1200  FUN_ffff000090718a4c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090718a4c(long param_1)

{
  undefined8 *puVar1;
  undefined *puVar2;
  long *plVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  long lVar7;
  long lVar8;
  ulong uVar9;
  ulong uVar10;
  long lVar11;
  long lVar12;
  long *plVar13;
  long **pplVar14;
  undefined8 uVar15;
  long lVar16;
  ulong uVar17;
  int local_74;
  undefined8 local_70;
  long *local_68;
  
  if (((param_1 == 0) || (iVar4 = FUN_ffff00009076c880(), iVar4 != 0)) ||
     (iVar4 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea5b1), iVar4 < 0)) {
    return 0xfffffff8;
  }
  local_74 = 0;
  local_70 = (long *)((ulong)local_70._4_4_ << 0x20);
  lVar7 = FUN_ffff00009076dcd4(param_1,iVar4,s_bootargs_ffff0000907dd913,&local_70);
  if (lVar7 == 0) {
    return 0xfffffff8;
  }
  lVar8 = FUN_ffff00009076dcd4(param_1,iVar4,s_bootargs_ext_ffff0000907d2768,&local_74);
  if (lVar8 == 0) {
    local_74 = 0;
  }
  uVar9 = FUN_ffff00009077d2e0(1,(long)(int)local_70 + (long)local_74);
  if (uVar9 == 0) {
    return 0xfffffff8;
  }
  FUN_ffff00009077e858(uVar9,lVar7,(long)(int)local_70);
  if (lVar8 != 0) {
    iVar5 = FUN_ffff00009077f434
                      (uVar9 + (long)(int)local_70 + -1,(long)local_74 + 1,&DAT_ffff0000907e2509,
                       lVar8);
    if (iVar5 != local_74) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_lib_kcmdline_kcmdline_c_ffff0000907f41f5,0xa8,
                 s_len____args_ext_len_ffff0000907ea6c0);
    }
  }
  lVar7 = FUN_ffff00009077d2e0(1,0x1000);
  if (lVar7 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_lib_kcmdline_kcmdline_c_ffff0000907f41f5,0x137,
               s_new_cmdline_buf____NULL_ffff0000907dc1ce);
  }
  FUN_ffff0000907175f4(&LAB_ffff000090818198,0xffffffff);
  local_70 = &local_70;
  local_68 = local_70;
  uVar17 = uVar9;
  if ((undefined **)PTR_LOOP_ffff000090818190 != &PTR_LOOP_ffff000090818188) {
    do {
      puVar2 = PTR_LOOP_ffff000090818190;
      puVar1 = *(undefined8 **)(PTR_LOOP_ffff000090818190 + 8);
      *puVar1 = *(undefined8 *)PTR_LOOP_ffff000090818190;
      *(undefined8 **)(*(long *)puVar2 + 8) = puVar1;
      lVar8 = *(long *)(puVar2 + 0x10);
      *(undefined8 *)puVar2 = 0;
      *(undefined8 *)(puVar2 + 8) = 0;
      uVar10 = FUN_ffff00009077f27c(uVar9,lVar8);
      if (((uVar10 == 0) || ((uVar10 != uVar9 && (*(char *)(uVar10 - 1) != ' ')))) ||
         ((lVar11 = FUN_ffff00009077ef88(lVar8), *(char *)(lVar8 + lVar11 + -1) != '=' &&
          ((*(char *)(uVar10 + lVar11) != ' ' && (*(char *)(uVar10 + lVar11) != '\0')))))) {
        thunk_FUN_ffff0000907c2ad8(lVar8);
        thunk_FUN_ffff0000907c2ad8(*(undefined8 *)(puVar2 + 0x18));
        thunk_FUN_ffff0000907c2ad8(puVar2);
      }
      else {
        plVar13 = &local_70;
        *(ulong *)(puVar2 + 0x20) = uVar10;
        pplVar14 = &local_68;
        if (local_68 != plVar13) {
          do {
            plVar13 = *pplVar14;
            if (plVar13 == &local_70) {
              plVar13 = &local_70;
              break;
            }
            pplVar14 = (long **)(plVar13 + 1);
          } while ((ulong)plVar13[4] <= uVar10);
        }
        *(long *)puVar2 = *plVar13;
        *(long **)(puVar2 + 8) = plVar13;
        *(undefined **)(*plVar13 + 8) = puVar2;
        *plVar13 = (long)puVar2;
      }
    } while ((undefined **)PTR_LOOP_ffff000090818190 != &PTR_LOOP_ffff000090818188);
    if (local_68 != &local_70) {
      lVar8 = 0;
      do {
        plVar3 = local_68;
        plVar13 = (long *)local_68[1];
        *plVar13 = *local_68;
        *(long **)(*local_68 + 8) = plVar13;
        *local_68 = 0;
        local_68[1] = 0;
        lVar12 = FUN_ffff00009077ef88(local_68[3]);
        lVar11 = (plVar3[4] - uVar17) + lVar8;
        lVar16 = lVar11 + lVar12;
        if (0xffe < lVar16) goto LAB_ffff000090718ee4;
        FUN_ffff00009077e858(lVar7 + lVar8,uVar17);
        FUN_ffff00009077e858(lVar7 + lVar11,plVar3[3],lVar12);
        uVar10 = FUN_ffff00009077edac(plVar3[4],0x20);
        if (uVar10 == 0) {
          lVar8 = FUN_ffff00009077ef88(uVar17);
          uVar10 = uVar17 + lVar8;
        }
        uVar17 = uVar10;
        thunk_FUN_ffff0000907c2ad8(plVar3[2]);
        thunk_FUN_ffff0000907c2ad8(plVar3[3]);
        thunk_FUN_ffff0000907c2ad8(plVar3);
        lVar8 = lVar16;
      } while (local_68 != &local_70);
      goto LAB_ffff000090718d6c;
    }
  }
  lVar16 = 0;
LAB_ffff000090718d6c:
  lVar8 = lVar7 + lVar16;
  lVar11 = FUN_ffff00009077ef88(uVar17);
  if (lVar11 != 0) {
    lVar16 = lVar11 + lVar16;
    if (0xffe < lVar16) {
LAB_ffff000090718ee4:
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18
                (s__ERROR__CMDLINE_overflow_tail____ffff0000907d0b9d,lVar16 + lVar7 + 1,
                 lVar7 + 0x1000);
    }
    iVar5 = FUN_ffff00009077f434(lVar8,lVar11 + 1,&DAT_ffff0000907dbe71,uVar17);
    if (iVar5 != (int)lVar11) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_lib_kcmdline_kcmdline_c_ffff0000907f41f5,0x116,
                 s_len_____int_size_ffff0000907e5be5);
    }
    lVar8 = lVar7 + lVar16;
  }
  if (_DAT_ffff000090843c10 != 0) {
    iVar5 = FUN_ffff00009077f434(lVar8,0x1000,&DAT_ffff0000907dbe71);
    lVar8 = _DAT_ffff000090843c10;
    iVar6 = FUN_ffff00009077ef88(_DAT_ffff000090843c10);
    if (iVar5 != iVar6) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_lib_kcmdline_kcmdline_c_ffff0000907f41f5,0x143,
                 s_len_____int_strlen_append_cmdlin_ffff0000907d2741);
    }
    thunk_FUN_ffff0000907c2ad8(lVar8);
    _DAT_ffff000090843c10 = 0;
  }
  iVar5 = FUN_ffff00009077ef88(lVar7);
  iVar4 = FUN_ffff00009076e40c(param_1,iVar4,s_bootargs_ffff0000907dd913,lVar7,iVar5 + 1);
  if (iVar4 == 0) {
    uVar15 = 0;
  }
  else {
    FUN_ffff00009077de54(s_fdt_setprop_error__ffff0000907d3fb8);
    uVar15 = 0xffffffff;
  }
  thunk_FUN_ffff0000907c2ad8(uVar9);
  thunk_FUN_ffff0000907c2ad8(lVar7);
  iVar4 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea5b1);
  if (-1 < iVar4) {
    local_70 = (long *)((ulong)local_70 & 0xffffffff00000000);
    FUN_ffff00009076dcd4(param_1,iVar4,s_bootargs_ffff0000907dd913,&local_70);
  }
  FUN_ffff00009071768c(&LAB_ffff000090818198);
  return uVar15;
}


