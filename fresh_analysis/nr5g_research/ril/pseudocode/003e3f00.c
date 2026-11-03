
/* RtcCapabilitySwitchUtil::getRafFromNetworkType(int) */

void RtcCapabilitySwitchUtil::getRafFromNetworkType(int param_1)

{
  long lVar1;
  undefined4 uVar2;
  ulong uVar3;
  undefined8 local_30;
  long local_28;
  
  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  uVar2 = 0x18e0e;
  switch(param_1) {
  case 0:
  case 3:
    break;
  case 1:
    uVar2 = 0x10006;
    break;
  case 2:
    uVar2 = 0x8e08;
    break;
  case 4:
    uVar2 = 0x31f0;
    break;
  case 5:
    uVar2 = 0x70;
    break;
  case 6:
    uVar2 = 0x3180;
    break;
  case 7:
    uVar2 = 0x1bffe;
    break;
  case 8:
    uVar2 = 0x871f0;
    break;
  case 9:
    uVar2 = 0x9ce0e;
    break;
  case 10:
    uVar2 = 0x9fffe;
    break;
  case 0xb:
  case 0x66:
    uVar2 = 0x84000;
    break;
  case 0xc:
    uVar2 = 0x8ce08;
    break;
  case 0xd:
    uVar2 = 0x20000;
    break;
  case 0xe:
    uVar2 = 0x28e08;
    break;
  case 0xf:
    uVar2 = 0xa4000;
    break;
  case 0x10:
    uVar2 = 0x30006;
    break;
  case 0x11:
    uVar2 = 0xb4006;
    break;
  case 0x12:
    uVar2 = 0x38e0e;
    break;
  case 0x13:
    uVar2 = 0xace08;
    break;
  case 0x14:
    uVar2 = 0xbce0e;
    break;
  case 0x15:
    uVar2 = 0x3bffe;
    break;
  case 0x16:
    uVar2 = 0xbfffe;
    break;
  case 0x17:
    uVar2 = 0x100000;
    break;
  case 0x18:
    uVar2 = 0x184000;
    break;
  case 0x19:
    uVar2 = 0x1871f0;
    break;
  case 0x1a:
    uVar2 = 0x19ce0e;
    break;
  case 0x1b:
    uVar2 = 0x19fffe;
    break;
  case 0x1c:
    uVar2 = 0x18ce08;
    break;
  case 0x1d:
    uVar2 = 0x1a4000;
    break;
  case 0x1e:
    uVar2 = 0x1b4006;
    break;
  case 0x1f:
    uVar2 = 0x1ace08;
    break;
  case 0x20:
    uVar2 = 0x1bce0e;
    break;
  case 0x21:
    uVar2 = 0x1bfffe;
    break;
  default:
    uVar3 = __rfx_is_gt_mode(0x18e0e);
    if ((uVar3 & 1) == 0) {
      mtkLogI("RtcCapa","getRafFromNetworkType, type=%d, raf=%d",param_1,1);
    }
    else {
      local_30 = 0;
      android::String8::format("%s%s",&local_30,&DAT_001fbebf,"RtcCapa");
      mtkLogI(local_30,"getRafFromNetworkType, type=%d, raf=%d",param_1,1);
      android::String8::~String8((String8 *)&local_30);
    }
    uVar2 = 1;
    break;
  case 0x65:
    uVar2 = 0x94006;
    break;
  case 0x67:
    uVar2 = 0x10076;
    break;
  case 0x68:
    uVar2 = 0x131f6;
    break;
  case 0x69:
    uVar2 = 0x971f6;
  }
  if (*(long *)(lVar1 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar2);
}


