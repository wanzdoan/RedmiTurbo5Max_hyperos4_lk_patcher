
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void ccci_get_md_check_hdr_inf(long param_1,undefined8 param_2)

{
  undefined2 *puVar1;
  ulong uVar2;
  bool bVar3;
  uint uVar4;
  code *pcVar5;
  uint uVar6;
  undefined4 uVar7;
  int iVar8;
  long lVar9;
  undefined8 uVar10;
  char *pcVar11;
  ulong uVar12;
  char *pcVar13;
  ulong uVar14;
  ulong uVar15;
  long lVar16;
  long lVar17;
  char *pcVar18;
  char *pcVar19;
  ulong uVar20;
  ulong uVar21;
  uint uVar22;
  undefined4 local_110;
  uint local_10c;
  undefined8 local_108;
  undefined8 uStack_100;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined4 local_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined2 local_70;
  long local_68;
  
  lVar9 = sp_el0;
  local_68 = *(long *)(lVar9 + 0x620);
  lVar9 = kmalloc_trace(_kobject_put,0xcc0,0x400);
  if (lVar9 == 0) {
    ccci_dump_write(0,1,"[0/util]fail to allocate memor for chk_hdr\n");
    ccci_dump_write(2,1,"[0/util]fail to allocate memor for chk_hdr\n");
  }
  else {
    uVar6 = get_raw_check_hdr(lVar9,0x400);
    uVar20 = (ulong)uVar6;
    if ((int)uVar6 < 0) {
      pcVar11 = "[0/util]fail to load header(%d)!\n";
      ccci_dump_write(0,1,"[0/util]fail to load header(%d)!\n",uVar20);
    }
    else {
      uVar7 = get_md_img_raw_size();
      *(undefined4 *)(param_1 + 0x50) = uVar7;
      local_110 = 0;
      get_md_resv_mem_info(0,&local_110,0,0);
      if (uVar20 - 0x401 < 0xfffffffffffffc03) {
LAB_001035d8:
                    /* WARNING: Does not return */
        pcVar5 = (code *)SoftwareBreakpoint(1,0x1035dc);
        (*pcVar5)();
      }
      lVar17 = lVar9 + uVar20;
      iVar8 = *(int *)(lVar17 + -4);
      ccci_dump_write(0,1,"[0/util]MD image header size = %d\n",iVar8);
      ccci_dump_write(2,1,"[0/util]MD image header size = %d\n",iVar8);
      if (iVar8 == 0x200) {
        local_10c = 0;
        get_md_resv_mem_info(0,&local_10c,0,0);
        if (uVar20 - 0x401 < 0xfffffffffffffc03) goto LAB_001035d8;
        uVar6 = *(uint *)(lVar17 + -4);
        uVar21 = (ulong)uVar6;
        ccci_dump_write(0,1,"[0/util]MD image header size = %d\n",uVar21);
        ccci_dump_write(2,1,"[0/util]MD image header size = %d\n",uVar21);
        if (uVar6 == 0x11c) {
          pcVar11 = (char *)&md_img_header_v4;
          uVar22 = 4;
          uVar12 = 0x11c;
LAB_00102d5c:
          uVar14 = uVar20 - uVar21;
          uVar15 = (ulong)(uVar6 - 1);
          uVar2 = uVar14;
          if (uVar14 < 0x401) {
            uVar2 = 0x400;
          }
          uVar20 = (uVar2 + uVar21) - uVar20;
          uVar2 = uVar12;
          if (uVar20 <= uVar12) {
            uVar2 = uVar20;
          }
          if (uVar15 <= uVar2) {
            uVar2 = uVar15;
          }
          pcVar13 = (char *)(lVar17 - uVar21);
          if (uVar2 < 2) {
            uVar20 = 0;
            pcVar18 = pcVar11;
          }
          else {
            uVar20 = uVar2 + 1 & 1;
            lVar17 = uVar14 + lVar9;
            uVar21 = 2;
            if (uVar20 != 0) {
              uVar21 = uVar20;
            }
            uVar20 = (uVar2 + 1) - uVar21;
            lVar16 = ~uVar2 + uVar21;
            pcVar19 = pcVar11 + 1;
            uVar14 = uVar14 + uVar20;
            pcVar13 = pcVar13 + uVar20;
            pcVar18 = pcVar11 + uVar20;
            lVar17 = lVar17 + 1;
            do {
              puVar1 = (undefined2 *)(lVar17 + -1);
              lVar16 = lVar16 + 2;
              lVar17 = lVar17 + 2;
              *(undefined2 *)(pcVar19 + -1) = *puVar1;
              pcVar19 = pcVar19 + 2;
            } while (lVar16 != 0);
          }
          lVar17 = uVar12 - uVar20;
          pcVar18 = pcVar18 + -1;
          do {
            if (((lVar17 == 0) || (0x3ff < uVar14)) || (uVar12 <= uVar20)) goto LAB_001035d8;
            uVar20 = uVar20 + 1;
            lVar17 = lVar17 + -1;
            uVar14 = uVar14 + 1;
            pcVar18 = pcVar18 + 1;
            *pcVar18 = *pcVar13;
            pcVar13 = pcVar13 + 1;
          } while (pcVar18 != pcVar11 + uVar15);
          ccci_dump_write(0,1,"[0/util]******MD image check v%d %d*****\n",
                          *(undefined4 *)(pcVar11 + 0xc),uVar6);
          ccci_dump_write(2,1,"[0/util]******MD image check v%d %d*****\n",
                          *(undefined4 *)(pcVar11 + 0xc),uVar6);
          iVar8 = strncmp(pcVar11,"CHECK_HEADER",0xc);
          if (iVar8 != 0) {
            ccci_dump_write(0,1,"[0/util]md check header not exist!\n");
            _printk(&DAT_0010fd11);
            goto LAB_00103390;
          }
          if (uVar22 < *(uint *)(pcVar11 + 0xc)) {
            ccci_dump_write(0,1,"[0/util][Error]md check header version mis-match to AP:[%d]!\n");
            _printk(&DAT_0010fb01,*(undefined4 *)(pcVar11 + 0xc));
            goto LAB_00103390;
          }
          if (*(uint *)(pcVar11 + 0xc) < 2) {
            bVar3 = true;
          }
          else {
            uVar6 = *(uint *)(pcVar11 + 0xac);
            if (uVar6 == local_10c) {
              bVar3 = true;
              uVar22 = uVar6;
            }
            else if (uVar6 < local_10c) {
              bVar3 = true;
              ccci_dump_write(0,1,
                              "[0/util][Warning]md size in md header isn\'t sync to DFO setting: (%08x, %08x)\n"
                             );
              ccci_dump_write(2,1,
                              "[0/util][Warning]md size in md header isn\'t sync to DFO setting: (%08x, %08x)\n"
                              ,*(undefined4 *)(pcVar11 + 0xac),local_10c);
              uVar6 = *(uint *)(pcVar11 + 0xac);
              uVar22 = local_10c;
            }
            else {
              bVar3 = false;
              uVar22 = local_10c;
            }
            *(uint *)(param_1 + 0xa0) = uVar6;
            *(uint *)(param_1 + 0xd8) = uVar22;
          }
          uVar10 = ccci_get_md_cap_str(*(undefined4 *)(pcVar11 + 0x14));
          *(undefined8 *)(param_1 + 0xb8) = uVar10;
          uVar10 = ccci_get_ap_platform();
          *(undefined8 *)(param_1 + 0xc0) = uVar10;
          uVar10 = ccci_get_md_cap_str(*(undefined4 *)(pcVar11 + 0x14));
          *(undefined8 *)(param_1 + 0x80) = uVar10;
          *(char **)(param_1 + 0x88) = pcVar11 + 0x18;
          *(char **)(param_1 + 0x90) = pcVar11 + 0x28;
          *(char **)(param_1 + 0x98) = pcVar11 + 0x68;
          uVar10 = ccci_get_md_product_str(*(undefined4 *)(pcVar11 + 0x10));
          *(undefined8 *)(param_1 + 0x78) = uVar10;
          *(undefined4 *)(param_1 + 0xa8) = *(undefined4 *)(pcVar11 + 0x10);
          *(undefined4 *)(param_1 + 0xac) = *(undefined4 *)(pcVar11 + 0xc);
          if (bVar3) {
            uVar6 = 0;
            pcVar13 = "[0/util]Modem header check OK!\n";
          }
          else {
            ccci_dump_write(0,1,"[0/util][Error]Modem header check fail!\n");
            ccci_dump_write(2,1,"[0/util][Error]Modem header check fail!\n");
            uVar6 = 0xfffffe65;
            pcVar13 = "[0/util][Reason]MD mem size mis-match to AP setting!\n";
          }
          ccci_dump_write(0,1,pcVar13);
          ccci_dump_write(2,1,pcVar13);
          ccci_dump_write(0,1,"[0/util](MD)[type]=%s, (AP)[type]=%s\n",
                          *(undefined8 *)(param_1 + 0x80),*(undefined8 *)(param_1 + 0xb8));
          ccci_dump_write(2,1,"[0/util](MD)[type]=%s, (AP)[type]=%s\n",
                          *(undefined8 *)(param_1 + 0x80),*(undefined8 *)(param_1 + 0xb8));
          ccci_dump_write(0,1,"[0/util](MD)[plat]=%s, (AP)[plat]=%s\n",
                          *(undefined8 *)(param_1 + 0x88),*(undefined8 *)(param_1 + 0xc0));
          ccci_dump_write(2,1,"[0/util](MD)[plat]=%s, (AP)[plat]=%s\n",
                          *(undefined8 *)(param_1 + 0x88),*(undefined8 *)(param_1 + 0xc0));
          if (1 < *(uint *)(pcVar11 + 0xc)) {
            ccci_dump_write(0,1,"[0/util](MD)[size]=%x, (AP)[size]=%x\n",
                            *(undefined4 *)(param_1 + 0xa0),*(undefined4 *)(param_1 + 0xd8));
            ccci_dump_write(2,1,"[0/util](MD)[size]=%x, (AP)[size]=%x\n",
                            *(undefined4 *)(param_1 + 0xa0),*(undefined4 *)(param_1 + 0xd8));
            uVar22 = *(uint *)(pcVar11 + 0xb0);
            if (uVar22 == 0) {
              uVar7 = 0;
            }
            else {
              if (*(uint *)(param_1 + 0x50) < uVar22) {
                local_70 = 0;
                local_80 = 0;
                uStack_78 = 0;
                local_90 = 0;
                uStack_88 = 0;
                local_a0 = 0;
                uStack_98 = 0;
                local_a8 = 0;
                local_b8 = 0;
                uStack_b0 = 0;
                local_c8 = 0;
                uStack_c0 = 0;
                local_d8 = 0;
                uStack_d0 = 0;
                local_e8 = 0;
                uStack_e0 = 0;
                local_f8 = 0;
                uStack_f0 = 0;
                local_108 = 0;
                uStack_100 = 0;
                scnprintf(&local_a0,0x32,"MD mem size smaller than image header setting");
                scnprintf(&local_108,100,
                          "MD mem cfg size(0x%x)<header size(0x%x),please check memory config in <chip>.dtsi"
                          ,*(undefined4 *)(param_1 + 0x50),*(undefined4 *)(pcVar11 + 0xb0));
                aed_md_exception_api(0,0,&local_108,100,&local_a0,0);
                ccci_dump_write(0,1,"[0/util][Reason]MD image size mis-match to AP!\n");
                ccci_dump_write(2,1,"[0/util][Reason]MD image size mis-match to AP!\n");
                uVar22 = *(uint *)(param_1 + 0x50);
                uVar6 = 0xfffffe65;
              }
              else {
                *(uint *)(param_1 + 0x50) = uVar22;
              }
              *(uint *)(param_1 + 0xdc) = uVar22;
              *(undefined4 *)(param_1 + 0xa4) = *(undefined4 *)(pcVar11 + 0xb0);
              uVar7 = *(undefined4 *)(pcVar11 + 0xb0);
            }
            ccci_dump_write(0,1,"[0/util](MD)[img_size]=%x, (AP)[img_size]=%x\n",uVar7);
            ccci_dump_write(2,1,"[0/util](MD)[img_size]=%x, (AP)[img_size]=%x\n",
                            *(undefined4 *)(pcVar11 + 0xb0),*(undefined4 *)(param_1 + 0x50));
          }
          ccci_dump_write(0,1,"[0/util](MD)[build_ver]=%s, [build_time]=%s\n",
                          *(undefined8 *)(param_1 + 0x98),*(undefined8 *)(param_1 + 0x90));
          ccci_dump_write(2,1,"[0/util](MD)[build_ver]=%s, [build_time]=%s\n",
                          *(undefined8 *)(param_1 + 0x98),*(undefined8 *)(param_1 + 0x90));
          ccci_dump_write(0,1,"[0/util](MD)[product_ver]=%s\n",*(undefined8 *)(param_1 + 0x78));
          ccci_dump_write(2,1,"[0/util](MD)[product_ver]=%s\n",*(undefined8 *)(param_1 + 0x78));
        }
        else {
          if (uVar6 == 0x200) {
            pcVar11 = (char *)&md_img_header_v6;
            uVar22 = 6;
            uVar12 = 0x200;
            goto LAB_00102d5c;
          }
LAB_00103390:
          uVar6 = 0xfffffe68;
        }
        pcVar11 = "[0/util]*****MD image check end******\n";
LAB_0010339c:
        ccci_dump_write(0,1,pcVar11);
        ccci_dump_write(2,1,pcVar11);
        if (-1 < (int)uVar6) {
          kfree(lVar9);
          scnprintf(md_img_info_str,0x100,"MD:%s*%s*%s*%s*%s\nAP:%s*%s*%08x (MD)%08x\n",
                    *(undefined8 *)(param_1 + 0x80),*(undefined8 *)(param_1 + 0x88),
                    *(undefined8 *)(param_1 + 0x98),*(undefined8 *)(param_1 + 0x90),
                    *(undefined8 *)(param_1 + 0x78),*(undefined8 *)(param_1 + 0xb8),
                    *(undefined8 *)(param_1 + 0xc0),*(undefined4 *)(param_1 + 0xd8),
                    *(undefined4 *)(param_1 + 0xa0));
          ccci_dump_write(0,1,"[0/util]check header str[%s]!\n",md_img_info_str);
          ccci_dump_write(2,1,"[0/util]check header str[%s]!\n",md_img_info_str);
          scnprintf(param_2,0x10,&DAT_0011152a,*(undefined8 *)(param_1 + 0x80));
          ccci_dump_write(0,1,"[0/util]post fix[%s]!\n",param_2);
          ccci_dump_write(2,1,"[0/util]post fix[%s]!\n",param_2);
          uVar10 = 0;
          goto LAB_001034c4;
        }
      }
      else {
        if (iVar8 == 0x11c) {
          local_10c = 0;
          get_md_resv_mem_info(0,&local_10c,0,0);
          memcpy(&md_img_header_v4,(void *)(lVar17 + -0x11c),0x11c);
          ccci_dump_write(0,1,"[0/util]**********MD image check V3 %zu**************\n",0x11c);
          ccci_dump_write(2,1,"[0/util]**********MD image check V3 %zu**************\n",0x11c);
          if (md_img_header_v4 == 0x45485f4b43454843 && DAT_0010cf58 == 0x52454441) {
            if (DAT_0010cf5c < 3) {
              ccci_dump_write(0,1,"[0/util][Error]md check header version mis-match to AP:[%d]!\n");
              _printk(&DAT_0010fb01,DAT_0010cf5c);
              uVar6 = 0;
            }
            else {
              if (DAT_0010cffc == local_10c) {
                bVar3 = true;
                uVar6 = DAT_0010cffc;
              }
              else if (DAT_0010cffc < local_10c) {
                bVar3 = true;
                ccci_dump_write(0,1,
                                "[0/util][Warning]md size in md header isn\'t sync to DFO setting: (%08x, %08x)\n"
                               );
                _printk(&DAT_0010ffe2,DAT_0010cffc,local_10c);
                uVar6 = local_10c;
              }
              else {
                bVar3 = false;
                uVar6 = local_10c;
              }
              uVar7 = DAT_0010cf64;
              *(uint *)(param_1 + 0xa0) = DAT_0010cffc;
              *(uint *)(param_1 + 0xd8) = uVar6;
              uVar10 = ccci_get_md_cap_str(uVar7);
              *(undefined8 *)(param_1 + 0xb8) = uVar10;
              uVar10 = ccci_get_ap_platform();
              uVar7 = DAT_0010cf64;
              *(undefined8 *)(param_1 + 0xc0) = uVar10;
              uVar10 = ccci_get_md_cap_str(uVar7);
              *(undefined8 *)(param_1 + 0x80) = uVar10;
              *(undefined **)(param_1 + 0x88) = &DAT_0010cf68;
              uVar7 = DAT_0010cf60;
              *(undefined **)(param_1 + 0x90) = &DAT_0010cf78;
              *(undefined **)(param_1 + 0x98) = &DAT_0010cfb8;
              uVar10 = ccci_get_md_product_str(uVar7);
              uVar7 = DAT_0010cf60;
              uVar6 = DAT_0010cf5c;
              *(undefined8 *)(param_1 + 0x78) = uVar10;
              *(undefined4 *)(param_1 + 0xa8) = uVar7;
              *(uint *)(param_1 + 0xac) = uVar6;
              if (bVar3) {
                ccci_dump_write(0,1,"[0/util]Modem header check OK!\n");
                ccci_dump_write(2,1,"[0/util]Modem header check OK!\n");
                uVar6 = 0;
              }
              else {
                ccci_dump_write(0,1,"[0/util][Error]Modem header check fail!\n");
                _printk(&DAT_00110037);
                ccci_dump_write(0,1,"[0/util][Reason]MD mem size mis-match to AP setting!\n");
                _printk(&DAT_00111313);
                uVar6 = 0xfffffe65;
              }
              ccci_dump_write(0,1,"[0/util](MD)[type]=%s, (AP)[type]=%s\n",
                              *(undefined8 *)(param_1 + 0x80),*(undefined8 *)(param_1 + 0xb8));
              ccci_dump_write(2,1,"[0/util](MD)[type]=%s, (AP)[type]=%s\n",
                              *(undefined8 *)(param_1 + 0x80),*(undefined8 *)(param_1 + 0xb8));
              ccci_dump_write(0,1,"[0/util](MD)[plat]=%s, (AP)[plat]=%s\n",
                              *(undefined8 *)(param_1 + 0x88),*(undefined8 *)(param_1 + 0xc0));
              ccci_dump_write(2,1,"[0/util](MD)[plat]=%s, (AP)[plat]=%s\n",
                              *(undefined8 *)(param_1 + 0x88),*(undefined8 *)(param_1 + 0xc0));
              if (1 < DAT_0010cf5c) {
                ccci_dump_write(0,1,"[0/util](MD)[size]=%x, (AP)[size]=%x\n",
                                *(undefined4 *)(param_1 + 0xa0),*(undefined4 *)(param_1 + 0xd8));
                ccci_dump_write(2,1,"[0/util](MD)[size]=%x, (AP)[size]=%x\n",
                                *(undefined4 *)(param_1 + 0xa0),*(undefined4 *)(param_1 + 0xd8));
                uVar22 = DAT_0010d000;
                if (DAT_0010d000 == 0) {
                  uVar22 = 0;
                }
                else {
                  if (*(uint *)(param_1 + 0x50) < DAT_0010d000) {
                    local_70 = 0;
                    local_80 = 0;
                    uStack_78 = 0;
                    local_90 = 0;
                    uStack_88 = 0;
                    local_a0 = 0;
                    uStack_98 = 0;
                    local_a8 = 0;
                    local_b8 = 0;
                    uStack_b0 = 0;
                    local_c8 = 0;
                    uStack_c0 = 0;
                    local_d8 = 0;
                    uStack_d0 = 0;
                    local_e8 = 0;
                    uStack_e0 = 0;
                    local_f8 = 0;
                    uStack_f0 = 0;
                    local_108 = 0;
                    uStack_100 = 0;
                    scnprintf(&local_a0,0x32,"MD mem size smaller than image header setting");
                    scnprintf(&local_108,100,
                              "MD mem size(0x%x)<header size(0x%x),please check memory config in <chip>.dtsi"
                              ,*(undefined4 *)(param_1 + 0x50),DAT_0010d000);
                    aed_md_exception_api(0,0,&local_108,100,&local_a0,0);
                    ccci_dump_write(0,1,"[0/util][Reason]MD image size mis-match to AP!\n");
                    _printk(&DAT_00110a89);
                    uVar22 = *(uint *)(param_1 + 0x50);
                    uVar6 = 0xfffffe65;
                  }
                  else {
                    *(uint *)(param_1 + 0x50) = DAT_0010d000;
                  }
                  uVar4 = DAT_0010d000;
                  *(uint *)(param_1 + 0xdc) = uVar22;
                  *(uint *)(param_1 + 0xa4) = uVar4;
                  uVar22 = DAT_0010d000;
                }
                ccci_dump_write(0,1,"[0/util](MD)[img_size]=%x, (AP)[img_size]=%x\n",uVar22);
                ccci_dump_write(2,1,"[0/util](MD)[img_size]=%x, (AP)[img_size]=%x\n",DAT_0010d000,
                                *(undefined4 *)(param_1 + 0x50));
              }
              ccci_dump_write(0,1,"[0/util](MD)[build_ver]=%s, [build_time]=%s\n",
                              *(undefined8 *)(param_1 + 0x98),*(undefined8 *)(param_1 + 0x90));
              ccci_dump_write(2,1,"[0/util](MD)[build_ver]=%s, [build_time]=%s\n",
                              *(undefined8 *)(param_1 + 0x98),*(undefined8 *)(param_1 + 0x90));
              ccci_dump_write(0,1,"[0/util](MD)[product_ver]=%s\n",*(undefined8 *)(param_1 + 0x78));
              ccci_dump_write(2,1,"[0/util](MD)[product_ver]=%s\n",*(undefined8 *)(param_1 + 0x78));
            }
          }
          else {
            ccci_dump_write(0,1,"[0/util]md check header not exist!\n");
            _printk(&DAT_0010fd11);
            uVar6 = 0xfffffe68;
          }
          pcVar11 = "[0/util]********MD image check V3*********\n";
          goto LAB_0010339c;
        }
        ccci_dump_write(0,1,"[0/util][Reason]Not support header version:%u!\n",0);
        _printk(&DAT_00112561,0);
        uVar6 = 0xfffffe65;
      }
      pcVar11 = "[0/util]check header fail(%d)!\n";
      ccci_dump_write(0,1,"[0/util]check header fail(%d)!\n",uVar6);
      uVar20 = (ulong)uVar6;
    }
    ccci_dump_write(2,1,pcVar11,uVar20);
    kfree(lVar9);
  }
  uVar10 = 0xffffffff;
LAB_001034c4:
  lVar9 = sp_el0;
  if (*(long *)(lVar9 + 0x620) == local_68) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar10);
}

