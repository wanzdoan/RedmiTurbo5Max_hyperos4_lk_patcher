
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint mtk_ccci_add_new_args(char *param_1,void *param_2,uint param_3,int param_4)

{
  uint uVar1;
  long lVar2;
  code *pcVar3;
  int iVar4;
  size_t sVar5;
  void *pvVar6;
  undefined *puVar7;
  undefined8 uVar8;
  ulong __n;
  ulong uVar9;
  ulong uVar10;
  uint uVar11;
  ulong uVar12;
  
  if ((s_init_done & 1) == 0) {
    puVar7 = &DAT_0011091a;
    uVar8 = 0x71;
    goto LAB_00108ebc;
  }
  if (param_1 == (char *)0x0) {
    puVar7 = &DAT_00112130;
    uVar8 = 0x76;
    goto LAB_00108ebc;
  }
  sVar5 = strlen(param_1);
  uVar11 = s_args_num;
  uVar1 = (int)sVar5 + 1;
  __n = (ulong)uVar1;
  if (0x24 < uVar1) {
    puVar7 = &DAT_00111b05;
    uVar8 = 0x7b;
    goto LAB_00108ebc;
  }
  if (param_2 == (void *)0x0) {
    puVar7 = &DAT_0011011d;
    uVar8 = 0x7f;
    goto LAB_00108ebc;
  }
  uVar12 = (ulong)s_args_num;
  if (s_args_num == 0) {
    if (param_4 == 0) {
      uVar12 = 0;
      goto LAB_00108df8;
    }
  }
  else {
    uVar9 = 0;
    uVar10 = 0x24;
    do {
      if (uVar9 == 0x49) goto LAB_00108f28;
      if (0xff8 < uVar10) {
                    /* WARNING: Does not return */
        pcVar3 = (code *)SoftwareBreakpoint(1,0x108f30);
        (*pcVar3)();
      }
      if ((*(uint *)(s_args_tbl + uVar10) == uVar1) &&
         (iVar4 = strncmp((char *)((long)&ccci_sib_1 + uVar10 + 4),param_1,__n), iVar4 == 0)) {
        if (-1 < (int)uVar9) {
          _printk(&DAT_0010f2df,"mtk_ccci_add_new_args",0x85,param_1);
          return 0xffffffff;
        }
        break;
      }
      uVar9 = uVar9 + 1;
      uVar10 = uVar10 + 0x38;
    } while (uVar12 != uVar9);
    if (param_4 == 0) {
      if (0x48 < uVar11) goto LAB_00108f28;
LAB_00108df8:
      uVar11 = s_args_num;
      if (uVar12 * -0x38 + 0xff8 < __n) goto LAB_00108f30;
      if (s_args_num < 0x49) {
        lVar2 = (ulong)s_args_num * 0x38;
        memcpy(s_args_tbl + lVar2,param_1,__n);
        *(uint *)(s_args_tbl + lVar2 + 0x24) = uVar1;
        *(uint *)(s_args_tbl + lVar2 + 0x28) = param_3;
        *(void **)(s_args_tbl + lVar2 + 0x30) = param_2;
        *(undefined4 *)(s_args_tbl + lVar2 + 0x2c) = 0;
        s_args_num = uVar11 + 1;
        return param_3;
      }
      goto LAB_00108f28;
    }
  }
  if (0x1000 < param_3) {
    puVar7 = &DAT_00112621;
    uVar8 = 0x96;
LAB_00108ebc:
    _printk(puVar7,"mtk_ccci_add_new_args",uVar8);
    return 0xffffffff;
  }
  if (s_curr_buf_free < param_3) {
    s_curr_buf_idx = s_curr_buf_idx + 1;
    if (7 < s_curr_buf_idx) {
      _printk(&DAT_00111b2f,"mtk_ccci_add_new_args",0x9e,8);
      return 0xffffffff;
    }
    pvVar6 = (void *)kmalloc_trace(_strlen,0xcc0,0x1000);
    if (7 < s_curr_buf_idx) goto LAB_00108f28;
    (&s_buf)[s_curr_buf_idx] = pvVar6;
    if (pvVar6 == (void *)0x0) {
      return 0xffffffff;
    }
    s_curr_buf_free = 0x1000;
    uVar11 = s_args_num;
    s_curr_ptr = pvVar6;
  }
  if (uVar11 < 0x49) {
    if ((ulong)uVar11 * -0x38 + 0xff8 < __n) {
LAB_00108f30:
                    /* WARNING: Subroutine does not return */
      fortify_panic("memcpy");
    }
    if (s_args_num < 0x49) {
      memcpy(s_args_tbl + (ulong)s_args_num * 0x38,param_1,__n);
      pvVar6 = s_curr_ptr;
      memcpy(s_curr_ptr,param_2,(ulong)param_3);
      if (s_args_num < 0x49) {
        lVar2 = (ulong)s_args_num * 0x38;
        s_args_num = s_args_num + 1;
        uVar11 = param_3 + (param_3 & 0xfffffff8) + 8;
        *(uint *)(s_args_tbl + lVar2 + 0x24) = uVar1;
        *(uint *)(s_args_tbl + lVar2 + 0x28) = param_3;
        *(void **)(s_args_tbl + lVar2 + 0x30) = pvVar6;
        iVar4 = 0;
        if (uVar11 <= s_curr_buf_free) {
          iVar4 = s_curr_buf_free - uVar11;
        }
        *(int *)(s_args_tbl + lVar2 + 0x2c) = param_4;
        s_curr_buf_free = iVar4;
        s_curr_ptr = (void *)((long)pvVar6 + (ulong)uVar11);
        return param_3;
      }
    }
  }
LAB_00108f28:
                    /* WARNING: Does not return */
  pcVar3 = (code *)SoftwareBreakpoint(0x5512,0x108f2c);
  (*pcVar3)();
}

