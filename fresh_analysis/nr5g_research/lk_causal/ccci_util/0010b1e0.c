
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void mtk_ccci_dump_md_mem_layout(void)

{
  undefined4 *puVar1;
  code *pcVar2;
  int iVar3;
  uint uVar4;
  long lVar5;
  undefined *puVar6;
  uint uVar7;
  ulong uVar8;
  undefined8 local_70;
  long local_68;
  
  lVar5 = sp_el0;
  local_68 = *(long *)(lVar5 + 0x620);
  local_70 = 0;
  iVar3 = mtk_ccci_find_args_val("md_bank0_base",&local_70,8);
  if (iVar3 < 1) {
    lVar5 = 0;
    puVar6 = &DAT_0010f9f1;
LAB_0010b3ac:
    _printk(puVar6,"mtk_ccci_dump_md_mem_layout");
  }
  else {
    lVar5 = kmalloc_trace(_kobject_put,0xcc0,0x400);
    if (lVar5 != 0) {
      uVar4 = mtk_ccci_find_args_val("md_mem_layout",lVar5,0x400);
      if (0 < (int)uVar4) {
        ccci_dump_write(0,1,"[0/util]------ Dump md layout(%u) ---------\n",(ulong)uVar4 / 0x18);
        ccci_dump_write(2,1,"[0/util]------ Dump md layout(%u) ---------\n",(ulong)uVar4 / 0x18);
        if (0x17 < uVar4) {
          uVar8 = 0;
          uVar7 = uVar4 / 0x18;
          if (uVar4 / 0x18 < 2) {
            uVar7 = 1;
          }
          do {
            if ((((uVar8 - 0x3f0 < 0xfffffffffffffc00) || (0x3ff < uVar8)) || (0x400 < uVar8 + 4))
               || ((uVar8 - 0x3f8 < 0xfffffffffffffc00 || (0x400 < uVar8 + 0xc)))) {
LAB_0010b420:
                    /* WARNING: Does not return */
              pcVar2 = (code *)SoftwareBreakpoint(1,0x10b424);
              (*pcVar2)();
            }
            puVar1 = (undefined4 *)(lVar5 + uVar8);
            ccci_dump_write(0,1,"[0/util]| 0x%016llx | 0x%08x | 0x%08x | 0x%08x | 0x%08x |\n",
                            *(undefined8 *)(lVar5 + uVar8 + 0x10),*puVar1,puVar1[1],puVar1[2],
                            *(undefined4 *)(lVar5 + uVar8 + 0xc));
            if ((((uVar8 - 0x3f0 < 0xfffffffffffffc00) || ((0x3ff < uVar8 || (0x400 < uVar8 + 4))))
                || (uVar8 - 0x3f8 < 0xfffffffffffffc00)) || (0x400 < uVar8 + 0xc))
            goto LAB_0010b420;
            ccci_dump_write(2,1,"[0/util]| 0x%016llx | 0x%08x | 0x%08x | 0x%08x | 0x%08x |\n",
                            *(undefined8 *)(lVar5 + uVar8 + 0x10),*(undefined4 *)(lVar5 + uVar8),
                            puVar1[1],puVar1[2],*(undefined4 *)(lVar5 + uVar8 + 0xc));
            uVar8 = uVar8 + 0x18;
          } while ((ulong)uVar7 * 0x18 != uVar8);
        }
        goto LAB_0010b3e0;
      }
      puVar6 = &DAT_00111d15;
      goto LAB_0010b3ac;
    }
  }
  ccci_dump_write(0,1,"[0/util]------ Dump md layout(NA) ---------\n");
  ccci_dump_write(2,1,"[0/util]------ Dump md layout(NA) ---------\n");
LAB_0010b3e0:
  kfree(lVar5);
  lVar5 = sp_el0;
  if (*(long *)(lVar5 + 0x620) != local_68) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}

