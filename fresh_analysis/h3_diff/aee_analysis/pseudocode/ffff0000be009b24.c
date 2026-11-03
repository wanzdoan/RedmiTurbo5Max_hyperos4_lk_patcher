
uint FUN_ffff0000be009b24(long *param_1,undefined1 *param_2,uint param_3)

{
  uint uVar1;
  undefined4 uVar2;
  undefined1 uVar3;
  uint uVar4;
  uint uVar5;
  undefined1 *puVar6;
  int iVar7;
  ulong uVar8;
  char *pcVar9;
  undefined8 uVar10;
  uint uVar11;
  uint uVar12;
  
  if ((ulong)param_1[1] < (ulong)(param_1[2] + (long)(int)param_3)) {
    FUN_ffff0000be05fd98
              (s__s__d__dump_size_>_allocated_siz_ffff0000be0b2c12,s_lba_write_cb_ffff0000be0bcc9c,
               0xe5);
  }
  else {
    param_1[2] = param_1[2] + (long)(int)param_3;
    if ((param_2 == (undefined1 *)0x0) && (param_3 == 0)) {
      uVar8 = (**(code **)(*param_1 + 0x20))
                        (*param_1,(ulong)*(uint *)(param_1 + 3) << 0xc,(long)param_1 + 0x102c,
                         *(undefined4 *)((long)param_1 + 0x24));
      if ((uVar8 & 1) != 0) {
        return 0;
      }
      FUN_ffff0000be05fd98
                (s__s__d__SDCard__Write_dump_data_f_ffff0000be0c285a,s_lba_write_cb_ffff0000be0bcc9c
                 ,0xee);
    }
    else if (param_2 != (undefined1 *)0x0) {
      uVar11 = *(uint *)((long)param_1 + 0x24);
      iVar7 = (int)param_1[5];
      uVar12 = param_3;
      while( true ) {
        uVar4 = iVar7 * 0x1000 - uVar11;
        uVar5 = uVar4;
        if (uVar12 <= uVar4) {
          uVar5 = uVar12;
        }
        puVar6 = param_2;
        uVar1 = 0;
        if (uVar4 <= uVar12) {
          uVar1 = uVar12 - uVar4;
        }
        for (; uVar5 != 0; uVar5 = uVar5 - 1) {
          uVar8 = (ulong)uVar11;
          uVar11 = uVar11 + 1;
          uVar3 = *puVar6;
          *(uint *)((long)param_1 + 0x24) = uVar11;
          *(undefined1 *)((long)param_1 + uVar8 + 0x102c) = uVar3;
          puVar6 = puVar6 + 1;
        }
        if (uVar11 == iVar7 * 0x1000) break;
LAB_ffff0000be009ca4:
        if ((int)uVar1 < 0) goto LAB_ffff0000be009d24;
        if (uVar12 <= uVar4) {
          return param_3;
        }
        param_2 = param_2 + (uVar12 - uVar1);
        uVar12 = uVar1;
      }
      uVar8 = (**(code **)(*param_1 + 0x20))
                        (*param_1,(ulong)*(uint *)(param_1 + 3) << 0xc,(long)param_1 + 0x102c);
      if ((uVar8 & 1) == 0) {
        pcVar9 = s__s__d__SDCard__Write_dump_data_f_ffff0000be0c285a;
        uVar10 = 0xc3;
      }
      else {
        iVar7 = FUN_ffff0000be01269c(param_1,(int)param_1[5]);
        *(int *)(param_1 + 4) = iVar7;
        if (-1 < iVar7) {
          if (iVar7 == 0x3fe) {
            iVar7 = FUN_ffff0000be01269c(param_1,(int)param_1[5]);
            *(int *)(param_1 + 4) = iVar7;
            if (iVar7 < 0) {
              pcVar9 = s__s__d__SDCard__Reading_1022_bidx_ffff0000be0bf3e6;
              uVar10 = 0xce;
              goto LAB_ffff0000be009d20;
            }
          }
          iVar7 = FUN_ffff0000be011ce8(param_1,iVar7);
          *(int *)(param_1 + 5) = iVar7;
          if (iVar7 != 0) {
            uVar11 = 0;
            uVar2 = *(undefined4 *)((long)param_1 + (long)(int)param_1[4] * 4 + 0x2c);
            *(undefined4 *)((long)param_1 + 0x24) = 0;
            *(undefined4 *)(param_1 + 3) = uVar2;
            goto LAB_ffff0000be009ca4;
          }
          goto LAB_ffff0000be009d24;
        }
        pcVar9 = s__s__d__SDCard__Reading_bidx_fail_ffff0000be0bcde4;
        uVar10 = 200;
      }
LAB_ffff0000be009d20:
      FUN_ffff0000be05fd98(pcVar9,s_Do_Store_or_Write_ffff0000be0c295b,uVar10);
LAB_ffff0000be009d24:
      FUN_ffff0000be05fd98
                (s__s__d__SDCard__Store_and_Write_f_ffff0000be0af3ce,s_lba_write_cb_ffff0000be0bcc9c
                 ,0xff);
    }
  }
  return 0xffffffff;
}


