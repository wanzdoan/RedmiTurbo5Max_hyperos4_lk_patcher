
undefined4 mtk_ccci_find_args_val(char *param_1,void *param_2,uint param_3)

{
  code *pcVar1;
  int iVar2;
  size_t sVar3;
  undefined1 *puVar4;
  undefined *puVar5;
  undefined8 uVar6;
  ulong uVar7;
  uint uVar8;
  ulong uVar9;
  long lVar10;
  
  if ((s_init_done & 1) == 0) {
    puVar4 = &DAT_0011091a;
    uVar6 = 0x42;
  }
  else {
    if (param_1 != (char *)0x0) {
      sVar3 = strlen(param_1);
      uVar8 = (int)sVar3 + 1;
      if (uVar8 < 0x25) {
        uVar7 = (ulong)s_args_num;
        if (s_args_num != 0) {
          lVar10 = 0;
          uVar9 = 0;
          do {
            if (lVar10 == 0xff8) goto LAB_00108bfc;
            if (lVar10 - 0xfd5U < 0xfffffffffffff007) goto LAB_00108c00;
            if ((*(uint *)(s_args_tbl + lVar10 + 0x24) == uVar8) &&
               (iVar2 = strncmp(s_args_tbl + lVar10,param_1,(ulong)uVar8), iVar2 == 0)) {
              uVar8 = (uint)uVar9;
              if (-1 < (int)uVar8) {
                if (param_2 == (void *)0x0) {
                  if (uVar8 < 0x49) {
                    if (uVar9 < 0x49) {
                      return *(undefined4 *)(s_args_tbl + lVar10 + 0x28);
                    }
                    goto LAB_00108c00;
                  }
                }
                else if (uVar8 < 0x49) {
                  if (uVar9 < 0x49) {
                    uVar7 = (ulong)*(uint *)(s_args_tbl + lVar10 + 0x28);
                    if (param_3 < *(uint *)(s_args_tbl + lVar10 + 0x28)) {
                      _printk(&DAT_0010f992,"mtk_ccci_find_args_val",0x5d,uVar7,param_3,param_1);
                      return 0xffffffff;
                    }
                    if (uVar9 < 0x49) {
                      if (*(int *)(s_args_tbl + lVar10 + 0x2c) == 0) {
                        if (0x48 < uVar9) goto LAB_00108c00;
                        __memcpy_fromio(param_2,*(undefined8 *)(s_args_tbl + lVar10 + 0x30),uVar7);
                      }
                      else {
                        if (0x48 < uVar9) goto LAB_00108c00;
                        memcpy(param_2,*(void **)(s_args_tbl + lVar10 + 0x30),uVar7);
                      }
                      if (uVar9 < 0x49) {
                        return *(undefined4 *)(s_args_tbl + lVar10 + 0x28);
                      }
                    }
                  }
LAB_00108c00:
                    /* WARNING: Does not return */
                  pcVar1 = (code *)SoftwareBreakpoint(1,0x108c04);
                  (*pcVar1)();
                }
LAB_00108bfc:
                    /* WARNING: Does not return */
                pcVar1 = (code *)SoftwareBreakpoint(0x5512,0x108c00);
                (*pcVar1)();
              }
              break;
            }
            lVar10 = lVar10 + 0x38;
            uVar9 = uVar9 + 1;
          } while (uVar7 * 0x38 != lVar10);
        }
        puVar5 = &DAT_00112152;
        uVar6 = 0x54;
      }
      else {
        puVar5 = &DAT_0010f069;
        uVar6 = 0x4d;
      }
      _printk(puVar5,"mtk_ccci_find_args_val",uVar6,param_1);
      return 0xffffffff;
    }
    puVar4 = &DAT_00112130;
    uVar6 = 0x47;
  }
  _printk(puVar4,"mtk_ccci_find_args_val",uVar6);
  return 0xffffffff;
}

