// ffff000090708284  size=360  FUN_ffff000090708284

void FUN_ffff000090708284(uint param_1)

{
  ulong uVar1;
  
  if (param_1 < 0x3f) {
    uVar1 = (ulong)param_1;
    switch(uVar1) {
    case 0:
      break;
    case 1:
      uVar1 = 1;
      break;
    case 2:
      uVar1 = 2;
      break;
    case 3:
      uVar1 = 3;
      break;
    case 4:
      uVar1 = 4;
      break;
    case 5:
      uVar1 = 5;
      break;
    case 6:
      uVar1 = 6;
      break;
    case 7:
      uVar1 = 7;
      break;
    default:
      goto code_r0xffff000090708308;
    case 9:
      uVar1 = 8;
      break;
    case 10:
      uVar1 = 9;
      break;
    case 0xb:
      uVar1 = 10;
      break;
    case 0xd:
      uVar1 = 0xb;
      break;
    case 0xe:
      uVar1 = 0xc;
      break;
    case 0xf:
      uVar1 = 0xd;
      break;
    case 0x10:
      uVar1 = 0xe;
      break;
    case 0x11:
      uVar1 = 0xf;
      break;
    case 0x14:
      uVar1 = 0x10;
      break;
    case 0x15:
      uVar1 = 0x11;
      break;
    case 0x16:
      uVar1 = 0x12;
      break;
    case 0x17:
      uVar1 = 0x13;
      break;
    case 0x21:
      uVar1 = 0x14;
      break;
    case 0x30:
      uVar1 = 0x15;
      break;
    case 0x3d:
      uVar1 = 0x16;
      break;
    case 0x3e:
      uVar1 = 0x17;
    }
    FUN_ffff00009077de54
              (&DAT_ffff0000907f0b1d,
               (&PTR_s_Address_size_fault__level_0_of_t_ffff000090816ae8)[uVar1 * 2]);
    return;
  }
code_r0xffff000090708308:
  return;
}


