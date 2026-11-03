
int FUN_ffff0000be022dc0(byte param_1,char param_2,long param_3,uint param_4,int param_5)

{
  undefined *puVar1;
  undefined *puVar2;
  int iVar3;
  char *pcVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  puVar2 = PTR_DAT_ffff0000be0f5618;
  if (param_1 < 0xf) {
    if (param_2 == '\0') {
      pcVar4 = s__s__d__addr_is_invalid__ffff0000be0b1c53;
      uVar5 = 0x2b2;
    }
    else {
      if (param_3 != 0) {
        uVar6 = (ulong)param_1;
        puVar1 = PTR_DAT_ffff0000be0f5618 + uVar6 * 0x90 + 0x48;
        iVar3 = FUN_ffff0000be01a730(puVar1,1);
        if (iVar3 != 0) {
          return -0x21;
        }
        *(ushort *)(puVar2 + uVar6 * 0x90 + 8) = (ushort)param_1;
        puVar2[uVar6 * 0x90 + 0x38] = param_2;
        *(undefined4 *)(puVar2 + uVar6 * 0x90 + 0x1c) = 3;
        puVar2[uVar6 * 0x90 + 0x3e] = 0;
        *(long *)(puVar2 + uVar6 * 0x90 + 0x30) = param_3;
        *(uint *)(puVar2 + uVar6 * 0x90 + 0x2c) = param_4 & 0xffff | param_5 << 0x10;
        iVar3 = FUN_ffff0000be022590();
        FUN_ffff0000be01a7c8(puVar1);
        if (-1 < iVar3) {
          return iVar3;
        }
        FUN_ffff0000be05fd98
                  (s__s__d__write_read_0x_x_bytes_fai_ffff0000be0af7ba,
                   s_mtk_i2c_write_read_ffff0000be0ab523,0x2cf,
                   *(uint *)(puVar2 + uVar6 * 0x90 + 0x2c),iVar3);
        return iVar3;
      }
      pcVar4 = s__s__d__data_buffer_is_NULL__ffff0000be0bf649;
      uVar5 = 0x2b7;
    }
  }
  else {
    pcVar4 = s__s__d__bus_id_is_invalid__ffff0000be0b0b5a;
    uVar5 = 0x2ad;
  }
  FUN_ffff0000be05fd98(pcVar4,s_mtk_i2c_write_read_ffff0000be0ab523,uVar5);
  return -8;
}


