
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009072782c(code *param_1)

{
  long lVar1;
  undefined4 uVar2;
  long lVar3;
  ulong uVar4;
  undefined8 uVar5;
  long lVar6;
  
  lVar3 = FUN_ffff00009077d514(0xb00c8);
  if (lVar3 != 0) {
    uVar4 = FUN_ffff000090727c90();
    if ((uVar4 & 1) == 0) {
      FUN_ffff00009077e11c
                (s__s__GPUEB_MFG0__NOT_power_on____ffff0000907e5fd2,
                 s__GPUEB__EXPDB__ffff0000907fb5a9);
    }
    else {
      FUN_ffff000090727ba0();
      _DAT_ffff00004b140100 = 0;
      if (_DAT_ffff00004b09fd0c != -0x21524111) {
        FUN_ffff00009077e11c
                  (s__s__gpueb_sram_data_invalid__PLT_ffff0000907f7f4e,
                   s__GPUEB__EXPDB__ffff0000907fb5a9,0x4b09fd0c,_DAT_ffff00004b09fd0c,0xdeadbeef);
      }
      FUN_ffff00009077ee50(lVar3,0,0xb00c8);
      if (_DAT_ffff00004b09fd00 == 0) {
        FUN_ffff00009077e11c
                  (s__s__GPUEB_INFO___0__skip_coredum_ffff0000907e77d9,
                   s__GPUEB__EXPDB__ffff0000907fb5a9);
      }
      _DAT_ffff00004b140100 = 0;
      lVar6 = 0;
      do {
        lVar1 = lVar6 + 8;
        uVar2 = *(undefined4 *)(&DAT_ffff00004b000004 + lVar6);
        *(undefined4 *)(lVar3 + lVar6) = *(undefined4 *)(&DAT_ffff00004b000000 + lVar6);
        ((undefined4 *)(lVar3 + lVar6))[1] = uVar2;
        lVar6 = lVar1;
      } while (lVar1 != 0xa0000);
      uVar4 = (ulong)_DAT_ffff00004b09fd58;
      if (_DAT_ffff00004b09fd58 < 0x9ef81) {
        *(undefined4 *)(lVar3 + 0xa0048) = *(undefined4 *)(&DAT_ffff00004b000000 + uVar4);
        *(undefined4 *)(lVar3 + 0xa004c) = *(undefined4 *)(&DAT_ffff00004b000004 + uVar4);
        *(undefined4 *)(lVar3 + 0xa0050) = *(undefined4 *)(uVar4 - 0xffffb4fffff8);
        *(undefined4 *)(lVar3 + 0xa0054) = *(undefined4 *)(uVar4 - 0xffffb4fffff4);
        *(undefined4 *)(lVar3 + 0xa0058) = *(undefined4 *)(uVar4 - 0xffffb4fffff0);
        *(undefined4 *)(lVar3 + 0xa005c) = *(undefined4 *)(uVar4 - 0xffffb4ffffec);
        *(undefined4 *)(lVar3 + 0xa0060) = *(undefined4 *)(uVar4 - 0xffffb4ffffe8);
        *(undefined4 *)(lVar3 + 0xa0064) = *(undefined4 *)(uVar4 - 0xffffb4ffffe4);
        *(undefined4 *)(lVar3 + 0xa0068) = *(undefined4 *)(uVar4 - 0xffffb4ffffe0);
        *(undefined4 *)(lVar3 + 0xa006c) = *(undefined4 *)(uVar4 - 0xffffb4ffffdc);
        *(undefined4 *)(lVar3 + 0xa0070) = *(undefined4 *)(uVar4 - 0xffffb4ffffd8);
        *(undefined4 *)(lVar3 + 0xa0074) = *(undefined4 *)(uVar4 - 0xffffb4ffffd4);
        *(undefined4 *)(lVar3 + 0xa0078) = *(undefined4 *)(uVar4 - 0xffffb4ffffd0);
        *(undefined4 *)(lVar3 + 0xa007c) = *(undefined4 *)(uVar4 - 0xffffb4ffffcc);
        *(undefined4 *)(lVar3 + 0xa0080) = *(undefined4 *)(uVar4 - 0xffffb4ffffc8);
        *(undefined4 *)(lVar3 + 0xa0084) = *(undefined4 *)(uVar4 - 0xffffb4ffffc4);
        *(undefined4 *)(lVar3 + 0xa0088) = *(undefined4 *)(uVar4 - 0xffffb4ffffc0);
        *(undefined4 *)(lVar3 + 0xa008c) = *(undefined4 *)(uVar4 - 0xffffb4ffffbc);
        *(undefined4 *)(lVar3 + 0xa0090) = *(undefined4 *)(uVar4 - 0xffffb4ffffb8);
        *(undefined4 *)(lVar3 + 0xa0094) = *(undefined4 *)(uVar4 - 0xffffb4ffffb4);
        *(undefined4 *)(lVar3 + 0xa0098) = *(undefined4 *)(uVar4 - 0xffffb4ffffb0);
        *(undefined4 *)(lVar3 + 0xa009c) = *(undefined4 *)(uVar4 - 0xffffb4ffffac);
        *(undefined4 *)(lVar3 + 0xa00a0) = *(undefined4 *)(uVar4 - 0xffffb4ffffa8);
        *(undefined4 *)(lVar3 + 0xa00a4) = *(undefined4 *)(uVar4 - 0xffffb4ffffa4);
        *(undefined4 *)(lVar3 + 0xa00a8) = *(undefined4 *)(uVar4 - 0xffffb4ffffa0);
        *(undefined4 *)(lVar3 + 0xa00ac) = *(undefined4 *)(uVar4 - 0xffffb4ffff9c);
        *(undefined4 *)(lVar3 + 0xa00b0) = *(undefined4 *)(uVar4 - 0xffffb4ffff98);
        *(undefined4 *)(lVar3 + 0xa00b4) = *(undefined4 *)(uVar4 - 0xffffb4ffff94);
        *(undefined4 *)(lVar3 + 0xa00b8) = *(undefined4 *)(uVar4 - 0xffffb4ffff90);
        *(undefined4 *)(lVar3 + 0xa00bc) = *(undefined4 *)(uVar4 - 0xffffb4ffff8c);
        *(undefined4 *)(lVar3 + 0xa00c0) = *(undefined4 *)(uVar4 - 0xffffb4ffff88);
        *(undefined4 *)(lVar3 + 0xa00c4) = *(undefined4 *)(uVar4 - 0xffffb4ffff84);
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__tbuffer_offset_0x_x__invalid_ffff0000908023be,
                   s__GPUEB__EXPDB__ffff0000907fb5a9);
      }
      FUN_ffff00009077eb20(lVar3 + 0xa0000,0xffff00004b140280,0x48);
      uVar5 = FUN_ffff00009077b334(lVar3);
      (*param_1)(uVar5,0xa00c8);
    }
    thunk_FUN_ffff0000907c314c(lVar3);
    return;
  }
  FUN_ffff00009077e11c
            (s__s__memory_allocat_for_output_fa_ffff0000907f45af,s__GPUEB__EXPDB__ffff0000907fb5a9);
  return;
}


