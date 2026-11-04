
/* copyFolder(std::string, std::string) */

bool copyFolder(byte *param_1,byte *param_2)

{
  ulong uVar1;
  ulong uVar2;
  uint uVar3;
  size_t sVar4;
  char *pcVar5;
  byte bVar6;
  long lVar7;
  int iVar8;
  DIR *__dirp;
  dirent *pdVar9;
  ulong *puVar10;
  void *pvVar11;
  FILE *__stream;
  undefined8 uVar12;
  void *pvVar13;
  byte *pbVar14;
  char *pcVar15;
  ulong local_170;
  ulong uStack_168;
  char *local_160;
  ulong local_150;
  ulong uStack_148;
  char *local_140;
  ulong local_130;
  ulong uStack_128;
  char *local_120;
  ulong local_110;
  ulong uStack_108;
  char *local_100;
  stat local_f0;

  lVar7 = tpidr_el0;
  local_f0.__unused[1] = *(long *)(lVar7 + 0x28);
  pbVar14 = *(byte **)(param_1 + 0x10);
  if ((*param_1 & 1) == 0) {
    pbVar14 = param_1 + 1;
  }
  __dirp = opendir((char *)pbVar14);
  if (__dirp != (DIR *)0x0) {
    pvVar13 = (void *)((ulong)&local_f0 | 1);
    pcVar15 = (char *)((ulong)&local_130 | 1);
LAB_00377c00:
    pdVar9 = readdir(__dirp);
    if (pdVar9 != (dirent *)0x0) {
      iVar8 = strcmp(pdVar9->d_name,".");
      if ((iVar8 != 0) && (iVar8 = strcmp(pdVar9->d_name,".."), iVar8 != 0)) {
        bVar6 = *param_1;
        local_110 = 0;
        uStack_108 = 0;
        local_100 = (char *)0x0;
        local_f0.st_ino = 0;
        local_f0.st_nlink = 0;
        sVar4 = (ulong)(bVar6 >> 1);
        if ((bVar6 & 1) != 0) {
          sVar4 = *(size_t *)(param_1 + 8);
        }
        local_f0.st_dev = 0;
        uVar1 = sVar4 + 1;
        if (uVar1 < 0xfffffffffffffff0) {
          if (uVar1 < 0x17) {
            local_f0.st_dev = (__dev_t)(byte)((int)uVar1 << 1);
            pvVar11 = pvVar13;
            if (sVar4 != 0) goto LAB_00377ca0;
          }
          else {
            pvVar11 = operator_new((uVar1 | 0xf) + 1);
            local_f0.st_dev = (uVar1 | 0xf) + 2;
            local_f0.st_ino = uVar1;
            local_f0.st_nlink = (__nlink_t)pvVar11;
LAB_00377ca0:
            pbVar14 = param_1 + 1;
            if ((bVar6 & 1) != 0) {
              pbVar14 = *(byte **)(param_1 + 0x10);
            }
            memmove(pvVar11,pbVar14,sVar4);
          }
          *(undefined2 *)((long)pvVar11 + sVar4) = 0x2f;
          strlen(pdVar9->d_name);
          puVar10 = (ulong *)std::string::append((char *)&local_f0,(ulong)pdVar9->d_name);
          local_100 = (char *)puVar10[2];
          uStack_108 = puVar10[1];
          local_110 = *puVar10;
          puVar10[1] = 0;
          puVar10[2] = 0;
          *puVar10 = 0;
          if ((local_f0.st_dev & 1) != 0) {
            operator_delete((void *)local_f0.st_nlink);
          }
          bVar6 = *param_2;
          local_130 = 0;
          uStack_128 = 0;
          local_120 = (char *)0x0;
          local_f0.st_ino = 0;
          local_f0.st_nlink = 0;
          sVar4 = (ulong)(bVar6 >> 1);
          if ((bVar6 & 1) != 0) {
            sVar4 = *(size_t *)(param_2 + 8);
          }
          local_f0.st_dev = 0;
          uVar1 = sVar4 + 1;
          if (uVar1 < 0xfffffffffffffff0) {
            if (uVar1 < 0x17) {
              local_f0.st_dev = (__dev_t)(byte)((int)uVar1 << 1);
              pvVar11 = pvVar13;
              if (sVar4 != 0) goto LAB_00377d70;
            }
            else {
              uVar2 = (uVar1 | 0xf) + 1;
              pvVar11 = operator_new(uVar2);
              local_f0.st_dev = uVar2 | 1;
              local_f0.st_ino = uVar1;
              local_f0.st_nlink = (__nlink_t)pvVar11;
LAB_00377d70:
              pbVar14 = param_2 + 1;
              if ((bVar6 & 1) != 0) {
                pbVar14 = *(byte **)(param_2 + 0x10);
              }
              memmove(pvVar11,pbVar14,sVar4);
            }
            *(undefined2 *)((long)pvVar11 + sVar4) = 0x2f;
            strlen(pdVar9->d_name);
            puVar10 = (ulong *)std::string::append((char *)&local_f0,(ulong)pdVar9->d_name);
            local_120 = (char *)puVar10[2];
            uStack_128 = puVar10[1];
            local_130 = *puVar10;
            puVar10[1] = 0;
            puVar10[2] = 0;
            *puVar10 = 0;
            if ((local_f0.st_dev & 1) != 0) {
              operator_delete((void *)local_f0.st_nlink);
            }
            pcVar5 = (char *)((ulong)&local_110 | 1);
            if ((local_110 & 1) != 0) {
              pcVar5 = local_100;
            }
            __stream = fopen(pcVar5,"r");
            local_f0.st_ino = 0;
            local_f0.st_dev = 0;
            local_f0.st_mode = 0;
            local_f0.st_uid = 0;
            local_f0.st_nlink = 0;
            local_f0.st_rdev = 0;
            local_f0.st_gid = 0;
            local_f0.__pad0 = 0;
            local_f0.st_blksize = 0;
            local_f0.st_size = 0;
            local_f0.st_atim.tv_sec = 0;
            local_f0.st_blocks = 0;
            local_f0.st_mtim.tv_sec = 0;
            local_f0.st_atim.tv_nsec = 0;
            local_f0.st_ctim.tv_sec = 0;
            local_f0.st_mtim.tv_nsec = 0;
            local_f0.__unused[0] = 0;
            local_f0.st_ctim.tv_nsec = 0;
            iVar8 = fileno(__stream);
            fstat(iVar8,&local_f0);
            uVar3 = (uint)local_f0.st_nlink & 0xf000;
            fclose(__stream);
            if (uVar3 == 0x4000) {
              pcVar5 = pcVar15;
              if ((local_130 & 1) != 0) {
                pcVar5 = local_120;
              }
              mkdir(pcVar5,0x1fd);
              pcVar5 = pcVar15;
              if ((local_130 & 1) != 0) {
                pcVar5 = local_120;
              }
              chown(pcVar5,1000,1000);
              pcVar5 = pcVar15;
              if ((local_130 & 1) != 0) {
                pcVar5 = local_120;
              }
              chmod(pcVar5,0x1f9);
              if ((local_110 & 1) == 0) {
                uStack_148 = uStack_108;
                local_150 = local_110;
                local_140 = local_100;
              }
              else {
                std::string::__init_copy_ctor_external((string *)&local_150,local_100,uStack_108);
              }
              if ((local_130 & 1) == 0) {
                uStack_168 = uStack_128;
                local_170 = local_130;
                local_160 = local_120;
              }
              else {
                std::string::__init_copy_ctor_external((string *)&local_170,local_120,uStack_128);
              }
              copyFolder(&local_150,&local_170);
              if ((local_170 & 1) != 0) {
                operator_delete(local_160);
              }
              if ((local_150 & 1) != 0) {
                operator_delete(local_140);
              }
            }
            else {
              copyFile((string *)&local_110,(string *)&local_130);
            }
            if ((local_130 & 1) != 0) {
              operator_delete(local_120);
            }
            if ((local_110 & 1) != 0) {
              operator_delete(local_100);
            }
            goto LAB_00377c00;
          }
        }
        else {
          if (*(long *)(lVar7 + 0x28) != local_f0.__unused[1]) goto LAB_0037807c;
          std::string::__throw_length_error_abi_ne180000_();
        }
        if (*(long *)(lVar7 + 0x28) == local_f0.__unused[1]) {
          uVar12 = std::string::__throw_length_error_abi_ne180000_();
          if ((local_150 & 1) != 0) {
            operator_delete(local_140);
          }
          if ((local_130 & 1) != 0) {
            operator_delete(local_120);
          }
          if ((local_110 & 1) != 0) {
            operator_delete(local_100);
          }
          if (*(long *)(lVar7 + 0x28) == local_f0.__unused[1]) {
                    /* WARNING: Subroutine does not return */
            _Unwind_Resume(uVar12);
          }
        }
        goto LAB_0037807c;
      }
      goto LAB_00377c00;
    }
    closedir(__dirp);
    sync();
  }
  if (*(long *)(lVar7 + 0x28) == local_f0.__unused[1]) {
    return __dirp != (DIR *)0x0;
  }
LAB_0037807c:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
