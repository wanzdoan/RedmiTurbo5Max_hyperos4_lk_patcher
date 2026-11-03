
void nc_smem_info_parsing_for_lk(void)

{
  ulong uVar1;
  ulong uVar2;
  undefined4 *puVar3;
  uint uVar4;
  code *pcVar5;
  int iVar6;
  uint uVar7;
  long lVar8;
  long lVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  undefined4 *puVar13;
  uint local_6c;
  long local_68;
  
  lVar8 = sp_el0;
  local_68 = *(long *)(lVar8 + 0x620);
  local_6c = 0;
  iVar6 = mtk_ccci_find_args_val("nc_smem_info_ext_num",&local_6c,4);
  if (iVar6 == 4) {
    uVar4 = local_6c * 0x10;
    uVar10 = (ulong)uVar4;
    lVar8 = __kmalloc(uVar10,0xdc0);
    if (lVar8 == 0) {
      ccci_dump_write(0,1,"[0/util]s_nc_update_layout:alloc nc_layout fail\n");
      _printk(&DAT_00110d7e);
    }
    else {
      uVar7 = mtk_ccci_find_args_val("nc_smem_info_ext",lVar8,uVar10);
      if (uVar7 == uVar4) {
        if (local_6c != 0) {
          uVar11 = 0;
          uVar12 = 0;
          do {
            uVar1 = uVar11 + 0xc;
            if ((((uVar10 < uVar1) || (uVar10 <= uVar11)) || (uVar10 < uVar11 + 4)) ||
               (uVar2 = uVar11 + 8, uVar10 < uVar2)) goto LAB_00109fec;
            puVar3 = (undefined4 *)(lVar8 + uVar11);
            lVar9 = lVar8 + uVar11;
            ccci_dump_write(0,1,"[0/util]nc_smem<%d>: ap:0x%08x md:0x%08x[0x%08x]\n",
                            *(undefined4 *)(lVar9 + 0xc),*puVar3,puVar3[1],puVar3[2]);
            if (((uVar10 < uVar1) || (uVar10 <= uVar11)) ||
               ((uVar10 < uVar11 + 4 ||
                ((uVar10 < uVar2 ||
                 (ccci_dump_write(2,1,"[0/util]nc_smem<%d>: ap:0x%08x md:0x%08x[0x%08x]\n",
                                  *(undefined4 *)(lVar9 + 0xc),*(undefined4 *)(lVar8 + uVar11),
                                  puVar3[1],puVar3[2]), uVar10 < uVar1)))))) goto LAB_00109fec;
            iVar6 = *(int *)(lVar9 + 0xc);
            if (DAT_00112c58 == iVar6) {
              lVar9 = 0;
              puVar13 = &DAT_00112c58;
LAB_00109ee0:
              if (uVar10 < uVar2) {
LAB_00109fec:
                    /* WARNING: Does not return */
                pcVar5 = (code *)SoftwareBreakpoint(1,0x109ff0);
                (*pcVar5)();
              }
              (&DAT_00112c60)[lVar9 * 10] = puVar3[2];
              ccci_dump_write(0,1,"[0/util]Update <%d> size 0x%08x\n");
              ccci_dump_write(2,1,"[0/util]Update <%d> size 0x%08x\n",*puVar13,
                              (&DAT_00112c60)[lVar9 * 10]);
            }
            else {
              puVar13 = &DAT_00112c80;
              if (DAT_00112c80 == iVar6) {
                lVar9 = 1;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112ca8;
              if (DAT_00112ca8 == iVar6) {
                lVar9 = 2;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112cd0;
              if (DAT_00112cd0 == iVar6) {
                lVar9 = 3;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112cf8;
              if (DAT_00112cf8 == iVar6) {
                lVar9 = 4;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112d20;
              if (DAT_00112d20 == iVar6) {
                lVar9 = 5;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112d48;
              if (DAT_00112d48 == iVar6) {
                lVar9 = 6;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112d70;
              if (DAT_00112d70 == iVar6) {
                lVar9 = 7;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112d98;
              if (DAT_00112d98 == iVar6) {
                lVar9 = 8;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112dc0;
              if (DAT_00112dc0 == iVar6) {
                lVar9 = 9;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112de8;
              if (DAT_00112de8 == iVar6) {
                lVar9 = 10;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112e10;
              if (DAT_00112e10 == iVar6) {
                lVar9 = 0xb;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112e38;
              if (DAT_00112e38 == iVar6) {
                lVar9 = 0xc;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112e60;
              if (DAT_00112e60 == iVar6) {
                lVar9 = 0xd;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112e88;
              if (DAT_00112e88 == iVar6) {
                lVar9 = 0xe;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112eb0;
              if (DAT_00112eb0 == iVar6) {
                lVar9 = 0xf;
                goto LAB_00109ee0;
              }
              puVar13 = &DAT_00112ed8;
              if (DAT_00112ed8 == iVar6) {
                lVar9 = 0x10;
                goto LAB_00109ee0;
              }
              if (DAT_00112f00 == iVar6) {
                lVar9 = 0x11;
                puVar13 = &DAT_00112f00;
                goto LAB_00109ee0;
              }
              if (DAT_00112f28 == iVar6) {
                lVar9 = 0x12;
                puVar13 = &DAT_00112f28;
                goto LAB_00109ee0;
              }
              if (DAT_00112f50 == iVar6) {
                lVar9 = 0x13;
                puVar13 = &DAT_00112f50;
                goto LAB_00109ee0;
              }
              if (DAT_00112f78 == iVar6) {
                lVar9 = 0x14;
                puVar13 = &DAT_00112f78;
                goto LAB_00109ee0;
              }
              if (DAT_00112fa0 == iVar6) {
                lVar9 = 0x15;
                puVar13 = &DAT_00112fa0;
                goto LAB_00109ee0;
              }
            }
            uVar12 = uVar12 + 1;
            uVar11 = uVar11 + 0x10;
          } while (uVar12 < local_6c);
        }
      }
      else {
        ccci_dump_write(0,1,"[0/util]nc_smem_info_ext get fail\n");
        _printk(&DAT_00110954);
      }
      kfree(lVar8);
    }
  }
  else {
    ccci_dump_write(0,1,"[0/util]nc_smem_info_ext_num get fail\n");
    _printk(&DAT_00111bc2);
  }
  lVar8 = sp_el0;
  if (*(long *)(lVar8 + 0x620) == local_68) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}

